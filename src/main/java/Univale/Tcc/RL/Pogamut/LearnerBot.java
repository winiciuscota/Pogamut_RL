package Univale.Tcc.RL.Pogamut;

//import com.thoughtworks.xstream.XStream;
import Univale.Tcc.RL.Pogamut.Actions.ActionEngage;
import Univale.Tcc.RL.Pogamut.Behaviors.Abstract.Behavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Agressive.AssaultBehavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Navigation.NavigationBehavior;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;

import java.util.logging.Level;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import Univale.Tcc.RL.Pogamut.Actions.ActionNavPoint;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;


@AgentScoped
public class LearnerBot extends LearnerBase
{

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


            float newQValue = Agent.update(CurrentState, Action, newState, 0);

            log.info("qValue adjustment is " + newQValue);
            Action newAction = Agent.getAction(newState);

            CurrentState = newState;
            Action = newAction;

            if (newAction instanceof ActionNavPoint) {
                NavPoint navPoint = navDict.get(((ActionNavPoint) newAction).getNavPoint());
                navigationBehavior.BehaviourDrivenMovement(navPoint);
            }
            else if (newAction instanceof ActionEngage) {
                assaultbehavior.BehaviourDrivenMovement();
            }

        }


    }

    //inicializa o(s) bot(s)
    public static void main(String args[]) throws PogamutException
    {
        new UT2004BotRunner(LearnerBot.class, "Learner").setMain(true).setLogLevel(Level.INFO).startAgents(1);
    }


}
