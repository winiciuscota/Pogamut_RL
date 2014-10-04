
package Univale.Tcc.RL.Pogamut.Actions;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.List;

/**
 *
 * @author winicius
 */
public class Action {
    private float QValue;


    public Action(){
        setQValue(0f);
    }


    public float getQValue() {
        return QValue;
    }

    public void setQValue(float QValue) {
        this.QValue = QValue;
    }

    public void updateQValue(float QValueAdjustment) {
        float oldQValue = getQValue();
        float newQValue = oldQValue + QValueAdjustment;
        setQValue(newQValue);
    }
}


