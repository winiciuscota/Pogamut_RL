package Univale.Tcc.RL.Pogamut.DecisionMaking;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import Univale.Tcc.RL.Pogamut.Actions.*;

import java.util.List;


/**
 *
 * @author winicius
 */
public final class GameState{



    private List<Action> availableActions;


    //contagem de inimigos(max 3)
    private int EnemiesCount;

    //Navpoint mais proximo do bot
    private UnrealId NavPoint;

    //id no navpoint anterior
    //isso é fundamental para determinar o proximo movimento
    //se o bot estava vindo do sul então ele pode concluir que ja coletou os itens daquela direção
    //e mover para uma localização menos explorada
    private UnrealId PreviousPosition;

    //a saude esta baixa?

    public UnrealId getPreviousPosition() {
        return PreviousPosition;
    }

    public void setPreviousPosition(UnrealId previousPosition) {
        PreviousPosition = previousPosition;
    }

    private boolean HealthLow;
    
    //estado sendo executado no momento
    private Action Action;

    private UnrealId NearestEnemyPosition;

    public UnrealId getNearestEnemyPosition() {
        return NearestEnemyPosition;
    }

    public void setNearestEnemyPosition(UnrealId nearestEnemyPosition) {
        NearestEnemyPosition = nearestEnemyPosition;
    }

    public GameState()
    {        
    }
    
    public GameState(int enemiesCount, boolean healthLow, Action action)
    {
        setEnemiesCount(enemiesCount);
        setHealthLow(healthLow);
        setAction(action);
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof GameState))return false;
        
        GameState otherState = (GameState)other;
        
        return this.getEnemiesCount() == otherState.getEnemiesCount()
                && this.getNearestEnemyPosition() == otherState.getNearestEnemyPosition()
                && this.isHealthLow() == otherState.isHealthLow()
                && this.getPreviousPosition() == otherState.getPreviousPosition()
                && this.getNavPoint() == otherState.getNavPoint();

    }

    /**
     * @return the enemiesCount
     */
    public int getEnemiesCount()
    {
        return EnemiesCount;
    }

    /**
     * @param enemiesCount the enemiesCount to set
     */
    public void setEnemiesCount(int enemiesCount)
    {
        this.EnemiesCount = (enemiesCount > 3 || enemiesCount < 0)? 3 : enemiesCount;
    }


    /**
     * @return the healthLow
     */
    public boolean isHealthLow()
    {
        return HealthLow;
    }

    /**
     * @param healthLow the healthLow to set
     */
    public void setHealthLow(boolean healthLow)
    {
        this.HealthLow = healthLow;
    }

    /**
     * @return the action
     */
    public Action getAction()
    {
        return Action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(Action action)
    {
        this.Action = action;
    }

    /**
     * @return the NavPoint
     */
    public UnrealId getNavPoint() {
        return NavPoint;
    }

    /**
     * @param NavPoint the NavPoint to set
     */
    public void setNavPoint(UnrealId NavPoint) {
        this.NavPoint = NavPoint;
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
    public double getqValue()
    {
        return getAvailableActions().stream().map(action -> action.getQValue()).max(Float::compareTo).orElse(0f);
    }


    public void updateActionQValue(Action action, float qValueAdjustment) throws ActionNotFoundException
    {
        if(!availableActions.contains(action))
            throw new ActionNotFoundException("action not found on the available actions list");

        availableActions.get(availableActions.indexOf(action)).updateQValue(qValueAdjustment);

    }

    public class ActionNotFoundException extends Exception {
        public ActionNotFoundException(String message) {
            super(message);
        }
    }
    
}
