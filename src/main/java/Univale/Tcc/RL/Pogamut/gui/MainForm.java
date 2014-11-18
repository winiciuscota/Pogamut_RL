package Univale.Tcc.RL.Pogamut.Gui;

import Univale.Tcc.RL.Pogamut.Bot.LearnerBase;
import Univale.Tcc.RL.Pogamut.Bot.LearnerBot;
import Univale.Tcc.RL.Pogamut.Bot.QLearningBot;
import Univale.Tcc.RL.Pogamut.Bot.SarsaBot;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.IAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.QLearningAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.SarsaAgent;
import com.google.inject.internal.Strings;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.jfree.chart.JFreeChart;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.jfree.experimental.chart.swt.ChartComposite;


import java.util.logging.Level;

/**
 * Created by winicius on 16/11/2014.
 */
public class MainForm extends Shell {


    UT2004BotRunner bot;
    private Display display;

    public MainForm(Display display) {
        super(display);

        InitUi();
    }

    @Override
    public void checkSubclass() {

    }

    public final void InitUi() {

        setMinimumSize(250, 200);
        setSize(250, 200);

        MigLayout layout = new MigLayout();

        setLayout(layout);
        Text text = new Text(this, SWT.PUSH);
        text.setText("Number of bots");
        Text numberOfBots = new Text(this, SWT.BORDER);
        numberOfBots.setLayoutData("wrap");
//        textb text = new Text(this, SWT.PUSH);

        Text text1 = new Text(this, SWT.PUSH);
        text1.setText("Learning algorithm");
        text1.setLayoutData("wrap");
        Button[] radios = new Button[2];
        radios[0] = new Button(this, SWT.RADIO);
        radios[0].setSelection(true);
        radios[0].setText("Q-Learning");
        radios[0].setLayoutData("wrap");

        radios[1] = new Button(this, SWT.RADIO);
        radios[1].setText("Sarsa");
        radios[1].setLayoutData("wrap");

        Button launchBotsButton = new Button(this, SWT.NONE);
        launchBotsButton.setText("Launch bots");
        Button generateChartbutton = new Button(this, SWT.PUSH);
        generateChartbutton.setText("Generate chart");
        generateChartbutton.setLayoutData("wrap");


        launchBotsButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                String botType;
                if (radios[0].getSelection() == true)
                    botType = "QLearningBot";
                else
                    botType = "SarsaAgent";

                try {
                    int nBots = Integer.parseInt(numberOfBots.getText());
                    LaunchBots(nBots, botType);
                } catch (Exception e) {
                    LaunchBots(1, botType);
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                throw new NotImplementedException();
            }
        });

        generateChartbutton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {

                DefaultLineChart chart = new DefaultLineChart();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                throw new NotImplementedException();
            }
        });

//        JFreeChart chart = new LineChart().getChart();
//        ChartComposite frame = new ChartComposite(this, SWT.NONE, chart, true);
//        frame.setLayoutData("span 20");


    }


    public void LaunchBots(int nBots, String botType) {

        if (botType == null || botType.equals("QLearningBot")) {
            bot = new UT2004BotRunner(QLearningBot.class, "QLearningBot");
        } else {
            bot = new UT2004BotRunner(SarsaBot.class, "SarsaBot");
        }
        bot.setLogLevel(Level.INFO);
        bot.startAgents(nBots);
    }

    public void GenerateChart() {

        //  demo.setVisible(true);

    }


}
