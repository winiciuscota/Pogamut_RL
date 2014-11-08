package Univale.Tcc.RL.Pogamut.DecisionMaking;

import Univale.Tcc.RL.Pogamut.Actions.*;

import java.util.List;
import java.util.Optional;


/**
 * @author winicius
 */
public final class GameState {


    private List<Action> availableActions;


    //contagem de inimigos(max 3)
    private int EnemiesCount;

    //Navpoint mais proximo do bot
    private String BotPosition;

    //id no navpoint anterior
    //isso é fundamental para determinar o proximo movimento
    //se o bot estava vindo do sul então ele pode concluir que ja coletou os itens daquela direção
    //e mover para uma localização menos explorada
    private String PreviousPosition;

    //a saude esta baixa?

    public String getPreviousPosition() {
        return PreviousPosition;
    }

    public void setPreviousPosition(String previousPosition) {
        PreviousPosition = previousPosition;
    }

    private boolean HealthLow;

    //estado sendo executado no momento

    private String NearestEnemyPosition;

    public String getNearestEnemyPosition() {
        return NearestEnemyPosition;
    }

    public void setNearestEnemyPosition(String nearestEnemyPosition) {
        NearestEnemyPosition = nearestEnemyPosition;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof GameState)) return false;

        GameState otherState = (GameState) other;

        return this.getEnemiesCount() == otherState.getEnemiesCount()
                && this.getNearestEnemyPosition().equals(otherState.getNearestEnemyPosition())
                && this.isHealthLow() == otherState.isHealthLow()
                && this.getPreviousPosition().equals(otherState.getPreviousPosition())
                && this.getBotPosition().equals(otherState.getBotPosition());
//        return true;
    }

    /**
     * @return the enemiesCount
     */
    public int getEnemiesCount() {
        return EnemiesCount;
    }

    /**
     * @param enemiesCount the enemiesCount to set
     */
    public void setEnemiesCount(int enemiesCount) {
        this.EnemiesCount = (enemiesCount > 3 || enemiesCount < 0) ? 3 : enemiesCount;
    }


    /**
     * @return the healthLow
     */
    public boolean isHealthLow() {
        return HealthLow;
    }

    /**
     * @param healthLow the healthLow to set
     */
    public void setHealthLow(boolean healthLow) {
        this.HealthLow = healthLow;
    }

    /**
     * @return the action
     */
    public Action getAction(Action action) throws ActionNotFoundException {


        Optional<Action> result = availableActions.stream().filter(a -> a.equals(action)).findFirst();

        if (!result.isPresent())
            throw new ActionNotFoundException("");
        return result.get();
    }


    /**
     * @return the NavPoint
     */
    public String getBotPosition() {
        return BotPosition;
    }

    /**
     * @param BotPosition the NavPoint to set
     */
    public void setBotPosition(String botPosition) {
        this.BotPosition = botPosition;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(List<Action> availableActions) {
        this.availableActions = availableActions;
    }

    /**
     * @return the qValue
     */
    public double getqValue() {
        return getAvailableActions().stream().map(action -> action.getQValue()).max(Double::compareTo).orElse(0d);
    }


    //public void updateActionQValue(Action action, float qValueAdjustment, double alpha) throws ActionNotFoundException
    public void updateActionQValue(Action action, float qValueAdjustment) throws ActionNotFoundException {
        if (!availableActions.contains(action))
            throw new ActionNotFoundException("action not found on the available actions list");

        //availableActions.get(availableActions.indexOf(action)).updateQValue(qValueAdjustment, alpha);
        availableActions.get(availableActions.indexOf(action)).updateQValue(qValueAdjustment);

    }

    public class ActionNotFoundException extends Exception {
        public ActionNotFoundException(String message) {
            super(message);
        }
    }

    public GameState()
    {
        setNearestEnemyPosition("");
        setBotPosition("");
        setPreviousPosition("");
    }
}
