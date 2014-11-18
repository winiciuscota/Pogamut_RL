package Univale.Tcc.RL.Pogamut.Services.Statistics;

import com.thoughtworks.xstream.XStream;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;


/**
 * Created by winicius on 09/11/2014.
 */
public class Statistics {
    private Timer timer;
    private List<RewardEvent> rewardEvents;


    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public List<RewardEvent> getRewardEvents() {
        return rewardEvents;
    }

    public void setRewardEvents(List<RewardEvent> rewardEvents) {
        this.rewardEvents = rewardEvents;
    }

    static final long ONE_MINUTE_IN_MILLIS = 60000;
    public Statistics()
    {
        setTimer(new Timer());

        XStream xstream = new XStream();
        try {

            FileInputStream file = new FileInputStream("rewards.xml");

            if (file == null)
                rewardEvents = new ArrayList<RewardEvent>();
            else {
                List<RewardEvent> rewards = (List<RewardEvent>) xstream.fromXML(file);
                file.close();
                if (rewards == null) {
                    rewards = new ArrayList<RewardEvent>();
                }
                setRewardEvents(rewards);

            }
        } catch (Exception e) {
            rewardEvents = new ArrayList<RewardEvent>();
        }
    }

    public void registerReward(Double rewardValue)
    {
        long elapsedTime = timer.getTotalElapsedTime();
        RewardEvent rewardEvent = new RewardEvent(rewardValue, elapsedTime);
        rewardEvents.add(rewardEvent);
    }

    public void SaveStatistics()
    {
        XStream xstream = new XStream();
        String rewardsXml = xstream.toXML(rewardEvents);

        Map<Long, Double> stastics = getStatistics(1);
        stastics = new TreeMap<Long, Double>(stastics);
        String statisticsXml = xstream.toXML(stastics);
        try
        {
            PrintWriter writer = new PrintWriter("rewards.xml", "UTF-8");
            writer.write(rewardsXml);
            writer.flush();
            writer.close();

            writer = new PrintWriter("statistics.xml", "UTF-8");

            writer.write(statisticsXml);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {

        }

        timer.SaveTimeInfo();
    }

    public Map<Long, Double>  getStatistics(int minutesInterval)
    {
        Map<Long, Double> result = new HashMap<Long,Double>();

        long interval = new Date(minutesInterval * ONE_MINUTE_IN_MILLIS).getTime();
        for(long i = interval; i < getTimer().getTotalElapsedTime() && i < ONE_MINUTE_IN_MILLIS * 300; i += interval )
        {
            final long finalI = i;
            result.put(new Long(i/ONE_MINUTE_IN_MILLIS), new Double(rewardEvents.stream().filter(reward -> reward.getElapsedTime() > finalI - interval &&
                    reward.getElapsedTime() < finalI).mapToDouble(reward -> reward.getReward()).summaryStatistics().getSum()));
        }

        return result;
    }


}
