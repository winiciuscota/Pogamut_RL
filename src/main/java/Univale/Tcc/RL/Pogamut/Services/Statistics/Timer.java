package Univale.Tcc.RL.Pogamut.Services.Statistics;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import com.thoughtworks.xstream.XStream;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by winicius on 08/11/2014.
 */
public class Timer {
    private long PreviousElapsedTime;

    private long StartDate;
    public long getTotalElapsedTime() {
        return PreviousElapsedTime + getCurrentGameElapsedTime();
    }

    public void setPreviousElapsedTime(long PreviousElapsedTime) {
        this.PreviousElapsedTime = PreviousElapsedTime;
    }

    public long getCurrentGameElapsedTime() {
        return new Date(new Date().getTime() - getStartDate()).getTime();
    }

    public Timer()
    {

        XStream xstream = new XStream();
        try {
            FileInputStream file = new FileInputStream("timer.xml");


            if (file == null)
                setPreviousElapsedTime(0);
            else {
                setPreviousElapsedTime((long)xstream.fromXML(file));
                file.close();

            }
        } catch (Exception e) {

        }

        setStartDate(new Date().getTime());
    }

    public Timer(long elapsedTime)
    {
        setPreviousElapsedTime(elapsedTime);
        setStartDate(new Date().getTime());
    }

    public void SaveTimeInfo()
    {
        XStream xstream = new XStream();
        String xml = xstream.toXML(getTotalElapsedTime());

        try
        {
            PrintWriter writer = new PrintWriter("timer.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {

        }

    }

    public long getStartDate() {
        return StartDate;
    }

    public void setStartDate(long startDate) {
        StartDate = startDate;
    }
}
