package Univale.Tcc.RL.Pogamut.DecisionMaking.Agent;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;

import java.util.*;

import Univale.Tcc.RL.Pogamut.Actions.Action;

/**
 * @author winicius_2
 */
public class QLearningAgent extends BaseAgent implements IAgent{

    public QLearningAgent() {
        super();
    }

    public QLearningAgent(double epsilon, double gamma, double alpha) {
        super(epsilon, gamma, alpha);
    }

    //retorna qvalue da ação informada no estado
    //retorna empty se o estado não for conhecido
    public Optional<Double> getQValue(GameState state, Action action) {

        Optional<Double> qValue = Optional.empty();

        try {

            Optional<GameState> state1 = states.stream().filter(st -> st.equals(state))
                    .findFirst();
            qValue = Optional.of(state1.get().getAction(action).getQValue());

        } catch (Exception e) {

        }

        return qValue;
    }

    //retorna qvalue maximo do estado
    Optional<Double> getMaxQValue(GameState state) {

        return states.stream().filter(s -> s.equals(state))
                .findFirst()
                .get().getAvailableActions().stream()
                .max((action1, action2) -> Double.compare(action1.getQValue(), action2.getQValue()))
                .map(action -> action.getQValue());

    }

    //retorna a melhor ação a ser tomada no estado informado
    Optional<Action> getBestAction(GameState state) {
        List<Action> actions = state.getAvailableActions();
        Collections.shuffle(actions);

        return actions.stream()
                .max((action1, action2) -> Double.compare(action1.getQValue(), action2.getQValue()));
    }

    //retorna a melhor ação ou uma ação aleatoria com probabilidade epsilon
    public Action getAction(GameState state) {

        Action result;

        List<Action> actions = state.getAvailableActions();

        // e-greedy - escolhe uma ação aleatoria com probabilidade epsilon
        Double probResult = randomNumberGenerator.nextDouble();
        if (probResult < epsilon) {
            result = actions.get(randomNumberGenerator.nextInt(actions.size()));
        } else {
            Optional<Action> action = getBestAction(state);

            result = action.get();
        }
        return result;
    }

    //atualiza o QValue
    public float update(GameState oldState, Action chosenAction, GameState newState, double reward) {

        if(states == null)
            return 0;

        if (states.contains(newState)) {
            newState = states.get(states.indexOf(newState));
        } else {
            states.add(newState);
        }

        if (oldState == null || chosenAction == null)
            return 0;
        GameState targetState = states.get(states.indexOf(oldState));

        //calculo do novo qValue no algortimo Q-learning
        //Q(s, a) = Q(s, a) + alpha(r + beta(max(Q(s', a')) - Q(s, a))
        double newStateQValue = newState.getMaximunQValue();
        double targetStateQValue = getAction(targetState).getQValue();
        float qValueAdjustment = 0;
        try {

            qValueAdjustment = (float) (alpha * (reward + (gamma * (newStateQValue - targetStateQValue))));

            targetState.updateActionQValue(chosenAction, qValueAdjustment);

        } catch (GameState.ActionNotFoundException e) {
        }

        return qValueAdjustment;
    }




}
