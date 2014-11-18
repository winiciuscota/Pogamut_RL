package Univale.Tcc.RL.Pogamut.DecisionMaking.Agent;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;

/**
 * Created by winicius on 15/11/2014.
 */
public interface IAgent {

    void setGamma(double gamma);
    void setAlpha(double alpha);
    void setEpsilon(double epsilon);

    void Save();

    float update(GameState CurrentState, Action Action, GameState newState, double reward);

    Action getAction(GameState state);

    int getCost();
}
