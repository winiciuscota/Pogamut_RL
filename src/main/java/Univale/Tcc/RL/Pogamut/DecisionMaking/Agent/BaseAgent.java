package Univale.Tcc.RL.Pogamut.DecisionMaking.Agent;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import com.thoughtworks.xstream.XStream;
import cz.cuni.amis.introspection.java.JProp;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

import Univale.Tcc.RL.Pogamut.Actions.Action;

/**
 * @author winicius
 */
public class BaseAgent{

    @JProp
    protected double epsilon;
    //probabilidade de exploração

    @JProp
    protected double gamma;
    //fator de desconto, determinado o quão preocupado o agente será com recompenas futuras

    @JProp
    protected double alpha;
    //taxa de aprendizagem

    Random randomNumberGenerator;

    //lista de todos os estados do jogo - base de conhecimento
    List<GameState> states;

    final XStream xstream = new XStream();

    public BaseAgent() {
        loadStates();

        //valores default para epsilon, gamma e alpha
        setEpsilon(0.1);
        setGamma(0.9);
        setAlpha(0.9);
    }

    public BaseAgent(double epsilon, double gamma, double alpha) {
        loadStates();
        setEpsilon(epsilon);
        setGamma(gamma);
        setAlpha(alpha);
    }

    private void loadStates()
    {
        FileInputStream file = null;
        try {
            file = new FileInputStream("db.xml");


            if (file == null)
                states = new ArrayList<GameState>();
            else {
                states = (List<GameState>) (xstream.fromXML(file));
                file.close();
                if (states == null) {
                    states = new ArrayList<GameState>();
                }
            }
        } catch (Exception e) {
            states = new ArrayList<GameState>();
        }
        randomNumberGenerator = new Random();
    }




    public int getCost() {
        return states.size();
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void Save() {
        XStream xstream = new XStream();
        String xml = xstream.toXML(states);


        try {
            PrintWriter writer = new PrintWriter("db.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }

    }
}
