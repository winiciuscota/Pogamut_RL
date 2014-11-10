package Univale.Tcc.RL.Pogamut.Services.Statistics;

import com.thoughtworks.xstream.XStream;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Statistics()
    {
        setTimer(new Timer());

        XStream xstream = new XStream();
        try {
            FileInputStream file = new FileInputStream("statistics.xml");

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
        String xml = xstream.toXML(rewardEvents);

        try
        {
            PrintWriter writer = new PrintWriter("statistics.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {

        }

        timer.SaveTimeInfo();
    }


}
