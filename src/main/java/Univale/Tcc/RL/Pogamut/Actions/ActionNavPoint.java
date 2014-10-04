package Univale.Tcc.RL.Pogamut.Actions;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.List;

/**
 * Created by winicius on 22/08/2014.
 */
public class ActionNavPoint extends Action{
    private UnrealId NavPoint;

    public UnrealId getNavPoint() {
        return NavPoint;
    }

    public void setNavPoint(UnrealId navPoint) {
        NavPoint = navPoint;
    }

    public ActionNavPoint(UnrealId navPoint) {
        NavPoint = navPoint;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ActionNavPoint))return false;
        else if(( ((ActionNavPoint) other).getNavPoint()) == this.getNavPoint())
            return true;
        return false;
    }
}
