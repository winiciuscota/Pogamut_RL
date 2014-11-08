
package Univale.Tcc.RL.Pogamut.Actions;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.List;

/**
 *
 * @author winicius
 */
public class Action {
    private double QValue;


    public Action(){
        setQValue(0f);
    }


    public double getQValue() {
        return QValue;
    }

    public void setQValue(double QValue) {
        this.QValue = QValue;
    }

    //public void updateQValue(float QValueAdjustment, double alpha) {
    public void updateQValue(float QValueAdjustment) {
        double oldQValue = getQValue();
        //double newQValue = ((1 - alpha) * oldQValue + QValueAdjustment);
        double newQValue = (oldQValue + QValueAdjustment);
        setQValue(newQValue);
    }
}


