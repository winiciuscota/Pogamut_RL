package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.SarsaAgent;

/**
 * Created by winicius on 17/11/2014.
 */
public class SarsaBot extends LearnerBot {
    public SarsaBot()
    {
        super(new SarsaAgent());
    }
}
