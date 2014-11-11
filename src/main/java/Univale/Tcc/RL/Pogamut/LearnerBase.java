package Univale.Tcc.RL.Pogamut;

import Univale.Tcc.RL.Pogamut.Behaviors.Agressive.AssaultBehavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Navigation.NavigationBehavior;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.LearningAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import Univale.Tcc.RL.Pogamut.Services.GameStateFactory.GameStateFactory;
import Univale.Tcc.RL.Pogamut.Services.Statistics.Statistics;
import Univale.Tcc.RL.Pogamut.Services.StuckDetector.StuckDetector;
import cz.cuni.amis.introspection.java.JProp;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.EventListener;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by winicius on 09/11/2014.
 */

@AgentScoped
public class LearnerBase extends UT2004BotModuleController<UT2004Bot> {

    @JProp
    public int frags = 0;

    @JProp
    public int deaths = 0;

    protected LearningAgent Agent;

    protected Map<String, NavPoint> navDict;
    protected Univale.Tcc.RL.Pogamut.Actions.Action Action;
    protected GameState CurrentState;
    protected Univale.Tcc.RL.Pogamut.Services.StuckDetector.StuckDetector StuckDetector;
    protected Univale.Tcc.RL.Pogamut.Services.GameStateFactory.GameStateFactory GameStateFactory;
    protected Statistics statistics;

    //behaviors
    AssaultBehavior assaultbehavior;
    NavigationBehavior navigationBehavior;

    IWorldEventListener<BotDamaged> botDamagedListener = new IWorldEventListener<BotDamaged>() {

        @Override
        public void notify(BotDamaged event) {
            log.info("bot has been damaged");
            //penalidade
            Double reward = -1d;

            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, reward);
            statistics.registerReward(reward);
        }
    };

    @EventListener(eventClass = PlayerDamaged.class)
    public void playerDamaged(PlayerDamaged event)
    {
        log.info("bot damaged an enemy");
        //recompença
        Double reward = 1d;

        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, reward);
        statistics.registerReward(reward);
    }

    @EventListener(eventClass = ItemPickedUp.class)
    public void itemPicketUp(ItemPickedUp event)
    {
        log.info("bot picked up an item " + event.getDescriptor().toString());
        //recompença
        if(navDict != null) {
            GameState state = GameStateFactory.getGameState(getWorldView());
            if(event.getType().getCategory().equals(ItemType.Category.WEAPON)) {
                Double reward = 5d;
                Agent.update(CurrentState, Action, state, reward);
                statistics.registerReward(reward);
            }
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
            Double reward = 2d;

            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, reward);
            statistics.registerReward(reward);
        }
    }

    //bot assassinado, deve registrar a penalidade
    @Override
    public void botKilled(BotKilled event)
    {
        log.info("bot was killed");
        deaths++;
        //penalidade
        Double reward = -2d;

        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, reward);
        statistics.registerReward(reward);
    }

    @Override
    public void prepareBot(UT2004Bot bot)
    {
        StuckDetector = new StuckDetector(4, navigation, info);

        Agent = new LearningAgent(); //iniciliza o algoritmo de aprendizagem

        CurrentState = null;

        Action = null;

        getWorldView().addEventListener(BotDamaged.class, botDamagedListener);

        statistics = new Statistics();

        assaultbehavior = new AssaultBehavior(players, shoot, navigation );
        navigationBehavior = new NavigationBehavior(shoot, navigation);
    }

    @Override
    public void beforeFirstLogic() {
        navDict = getWorldView().getAll(NavPoint.class).values().stream().collect(Collectors.toMap(nav -> nav.getId().toString(), nav -> nav));
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
        statistics.SaveStatistics();
    }

    //reseta a situação do bot
    protected void reset()
    {
        log.info(" warning - RESET");
        CurrentState = null;
    }
}
