package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.Behaviors.Agressive.AssaultBehavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Navigation.NavigationBehavior;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.IAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.QLearningAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import Univale.Tcc.RL.Pogamut.Services.Statistics.Statistics;
import cz.cuni.amis.introspection.java.JProp;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
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


    protected IAgent Agent;

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

    IWorldEventListener<ItemPickedUp> itemPickedUpListener = new IWorldEventListener<ItemPickedUp>() {

        @Override
        public void notify(ItemPickedUp event) {

            //recompença
            if (navDict != null) {
                double reward;
                GameState state = GameStateFactory.getGameState(getWorldView());
                log.info("bot picked up an item " + event.getDescriptor().toString());
                if (event.getType().getCategory().equals(ItemType.Category.WEAPON))
                    reward = 5;
                else
                    reward = 1;
                Agent.update(CurrentState, Action, state, reward);
                statistics.registerReward(reward);
            }
        }
    };

    IWorldEventListener<PlayerDamaged> playerDamagedListener = new IWorldEventListener<PlayerDamaged>() {

        @Override
        public void notify(PlayerDamaged event) {
            log.info("bot damaged an enemy");
            //recompença
            Double reward = 1d;

            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, reward);
            statistics.registerReward(reward);
        }
    };

    IWorldEventListener<PlayerKilled> playerKilledListerner = new IWorldEventListener<PlayerKilled>() {

        @Override
        public void notify(PlayerKilled event) {
            if (event.getKiller() == info.getId()) {
                log.info("bot killed an enemy");
                frags++;
                //recompença
                Double reward = 5d;

                GameState state = GameStateFactory.getGameState(getWorldView());
                Agent.update(CurrentState, Action, state, reward);
                statistics.registerReward(reward);
            }
        }
    };





    //bot assassinado, deve registrar a penalidade
    @Override
    public void botKilled(BotKilled event)
    {
        log.info("bot was killed");
        deaths++;
        //penalidade
        Double reward = -5d;

        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, reward);
        statistics.registerReward(reward);
    }



    @Override
    public void prepareBot(UT2004Bot bot)
    {
        StuckDetector = new Univale.Tcc.RL.Pogamut.Services.StuckDetector.StuckDetector(4, navigation, info);

        CurrentState = null;

        Action = null;


        //registrando event listerners
        getWorldView().addEventListener(BotDamaged.class, botDamagedListener);
        getWorldView().addEventListener(ItemPickedUp.class, itemPickedUpListener);
        getWorldView().addEventListener(PlayerDamaged.class, playerDamagedListener);
        getWorldView().addEventListener(PlayerKilled.class, playerKilledListerner);

        statistics = new Statistics();

        assaultbehavior = new AssaultBehavior(players, shoot, navigation );
        navigationBehavior = new NavigationBehavior(shoot, navigation);
    }

    @Override
    public void beforeFirstLogic() {
        navDict = getWorldView().getAll(NavPoint.class).values().stream().collect(Collectors.toMap(nav -> nav.getId().toString(), nav -> nav));
        GameStateFactory = new Univale.Tcc.RL.Pogamut.Services.GameStateFactory.GameStateFactory(players, info);
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
