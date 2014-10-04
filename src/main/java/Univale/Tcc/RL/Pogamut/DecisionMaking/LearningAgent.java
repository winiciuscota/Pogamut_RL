package Univale.Tcc.RL.Pogamut.DecisionMaking;

import com.thoughtworks.xstream.XStream;
import cz.cuni.amis.introspection.java.JProp;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import javax.xml.transform.Transformer;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 *
 * @author winicius_2
 */
public class LearningAgent
{

    @JProp
    double epsilon = 0.2;
    //probabilidade de exploração

    @JProp
    double beta = 0.9;
    //fator de desconto

    @JProp
    double alpha = 0.9;
    //taxa de aprendizagem

    Random randomNumberGenerator;

    //lista de todos os estados do jogo - base de conhecimento
    List<GameState> states;

    final XStream xstream = new XStream();
    public LearningAgent() {
        FileInputStream file = null;
        try {
            file = new FileInputStream("db.xml");


            if (file == null)
                states = new ArrayList<GameState>();
            else {
                //states = (List<GameState>) (xstream.fromXML(file));
                file.close();
                if(states == null)
                {
                    states = new ArrayList<GameState>();
                }
            }
        }

    catch(Exception e)
    {

    }
        randomNumberGenerator = new Random();

    }

    //retorna qvalue da ação informada no estado
    //retorna empty se o estado não for conhecido
    public Optional<Double> getQValue(GameState state, Action action)
    {
        return states.stream().filter(e -> e.equals(state))
                .filter(e -> e.getAction() == action)
                .map(e -> e.getqValue())
                .findFirst();
    }

    //retorna qvalue maximo do estado
    Optional<Double> getValue(GameState state)
    {

        return states.stream().filter(s -> s.equals(state))
                .sorted((s1, s2) -> Double.compare(s1.getqValue(), s2.getqValue()))
                .findFirst()
                .map(s -> s.getqValue());
    }

    //retorna a melhor ação a ser tomada no estado informado
    Optional<Action> getPolicy(GameState state)
    {
        return state.getAvailableActions().stream()
                .max((action1, action2) -> Double.compare(action1.getQValue(), action2.getQValue()));
    }

    //retorna a melhor ação ou uma ação aleatoria com probabilidade epsilon
    public Action getAction(GameState state)
    {

        Optional<Action> action = getPolicy(state);

        Action result;

        result = action.get();

        List<Action> actions = state.getAvailableActions();
        // e-reedy - escolhe uma ação aleatoria com probabilidade epsilon
        Double probResult = randomNumberGenerator.nextDouble();
        if (probResult < epsilon) {
            result = actions.get(randomNumberGenerator.nextInt(actions.size()));
        }
        return result;
    }

    //atualiza o QValue
    public float update(GameState oldState, Action chosenAction, GameState newState, double reward)
    {


        if (states.contains(newState)) {
            newState = states.get(states.indexOf(newState));
        }
        else
        {
            states.add(newState);
        }

        if(oldState == null || chosenAction == null)
            return 0;
        GameState targetState = states.get(states.indexOf(oldState));

        //calculo do novo qValue
        //Q(s, a) = (1 - alpha) Q(s, a) + alpha(r + beta(max(Q(s', a')))
        float qValueAdjustment = (float) ((1 - alpha) * (alpha * (reward + beta * newState.getqValue())));

        try
        {
            targetState.updateActionQValue(chosenAction, qValueAdjustment);

        }
        catch(GameState.ActionNotFoundException e)
        {
        }

        return qValueAdjustment;
    }

    public void Save()
    {
        XStream xstream = new XStream();
        String xml = xstream.toXML(states);


        try
        {
            PrintWriter writer = new PrintWriter("db.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {

        }

    }
   
    public int getCost()
    {
        return states.size();
    }

}
