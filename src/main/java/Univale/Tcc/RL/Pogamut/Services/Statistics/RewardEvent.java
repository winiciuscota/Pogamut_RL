package Univale.Tcc.RL.Pogamut.Services.Statistics;

/**
 * Created by winicius on 09/11/2014.
 */
public class RewardEvent {
    private Double Reward;
    private long elapsedTime;

    public Double getReward() {
        return Reward;
    }

    public void setReward(Double reward) {
        Reward = reward;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long time) {
        elapsedTime = time;
    }

    public RewardEvent(double reward, long time)
    {
        setReward(reward);
        setElapsedTime(time);
    }
}
