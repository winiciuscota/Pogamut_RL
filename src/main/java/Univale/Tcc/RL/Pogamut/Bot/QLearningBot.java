package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.QLearningAgent;

/**
 * Created by winicius on 17/11/2014.
 */
public class QLearningBot extends LearnerBot {
    public QLearningBot()
    {
        super(new QLearningAgent());
    }
}
