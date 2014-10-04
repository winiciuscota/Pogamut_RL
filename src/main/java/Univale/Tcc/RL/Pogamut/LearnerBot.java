package Univale.Tcc.RL.Pogamut;

//import com.thoughtworks.xstream.XStream;
import Univale.Tcc.RL.Pogamut.Actions.ActionEngage;
import Univale.Tcc.RL.Pogamut.Services.GameStateFactory;
import cz.cuni.amis.introspection.java.JProp;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.EventListener;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004PathAutoFixer;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;

import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import Univale.Tcc.RL.Pogamut.Actions.ActionNavPoint;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState;
import Univale.Tcc.RL.Pogamut.DecisionMaking.LearningAgent;
import Univale.Tcc.RL.Pogamut.Services.StuckDetector;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;


@AgentScoped
public class LearnerBot extends UT2004BotModuleController<UT2004Bot>
{

    @JProp
    public int frags = 0;

    @JProp
    public int deaths = 0;

    private LearningAgent Agent;

    private Map<UnrealId, NavPoint> navDict;
    private Action Action;
    private GameState CurrentState;
    private StuckDetector StuckDetector;
    private GameStateFactory GameStateFactory;

    IWorldEventListener<BotDamaged> botDamagedListener = new IWorldEventListener<BotDamaged>() {

        @Override
        public void notify(BotDamaged event) {
            log.info("bot has been damaged");
            //penalidade
            GameState state = GameStateFactory.getGameState(getWorldView());
            //
            Agent.update(CurrentState, Action, state, 1);
        }
    };

    @EventListener(eventClass = PlayerDamaged.class)
    public void playerDamaged(PlayerDamaged event)
    {
        log.info("bot damaged an enemy");
        //recompença
        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, 1);
    }

    @EventListener(eventClass = ItemPickedUp.class)
    public void itemPicketUp(ItemPickedUp event)
    {
        log.info("bot picked up an item " + event.getDescriptor().toString());
        //recompença
        if(navDict != null) {
            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, 1);
        }
    }

    //bot fez um frag, o algoritmo de aprendizagem deve registrar a recompença
    @EventListener(eventClass = PlayerKilled.class)
    public void playerKilled(PlayerKilled event)
    {
        if (event.getKiller() == info.getId())
        {
            log.info("bot killed an enemy");
            frags++;
            //recompença
            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, 2);
        }
    }

    //bot assassinado, deve registrar a penalidade
    @Override
    public void botKilled(BotKilled event)
    {
        log.info("bot was killed");
        deaths++;
        //penalidade
        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, -2);
    }

    @Override
    public void prepareBot(UT2004Bot bot)
    {
        StuckDetector = new StuckDetector(4, navigation, info);

        Agent = new LearningAgent(); //iniciliza o algoritmo de aprendizagem

        CurrentState = null;

        Action = null;

        getWorldView().addEventListener(BotDamaged.class, botDamagedListener);
    }

    @Override
    public void beforeFirstLogic() {
        navDict = getWorldView().getAll(NavPoint.class).values().stream().collect(Collectors.toMap(NavPoint::getId, nav -> nav));
        GameStateFactory = new GameStateFactory(players, info);
    }

    @Override
    public Initialize getInitializeCommand()
    {
        // skill determina a precisão de tiro do bot
        return new Initialize().setName("Learner").setSkin("Remus").setDesiredSkill(5);

    }

    @Override
    public void botShutdown() {
        log.info("bot destroyed, saving knowledge");
        Agent.Save();
    }

    //reseta a situação do bot
    protected void reset()
    {
        log.info(" warning - RESET");
        CurrentState = null;
    }

    @Override
    public void logic()
    {

        GameState newState = GameStateFactory.getGameState(getWorldView());

        log.log(Level.INFO, "knowledge size is {0}", Agent.getCost());

        if(StuckDetector.check())
        {
            reset();
        }

        if(!newState.equals(CurrentState)) {


            float newQValue = Agent.update(CurrentState, Action, newState, -0.1);

            log.info("qValue adjustment is " + newQValue);
            Action newAction = Agent.getAction(newState);

            CurrentState = newState;
            Action = newAction;

            if (newAction instanceof ActionNavPoint) {
                shoot.stopShooting();
                navigation.navigate(navDict.get(((ActionNavPoint) newAction).getNavPoint()));

            }
            else if (newAction instanceof ActionEngage) {

                Player enemy = players.getNearestVisiblePlayer(players.getVisibleEnemies().values());
                shoot.shoot(enemy);
            }

        }


    }

    //inicializa o(s) bot(s)
    public static void main(String args[]) throws PogamutException
    {

        new UT2004BotRunner(LearnerBot.class, "Learner").setMain(true).setLogLevel(Level.INFO).startAgents(1);
    }


}
