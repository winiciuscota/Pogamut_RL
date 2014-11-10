package Univale.Tcc.RL.Pogamut.Behaviors.Agressive;

import Univale.Tcc.RL.Pogamut.Behaviors.Abstract.Behavior;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.Players;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.IUT2004Navigation;
import cz.cuni.amis.pogamut.ut2004.bot.command.ImprovedShooting;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * Created by winicius on 09/11/2014.
 */
public class AssaultBehavior extends Behavior {

    public AssaultBehavior(Players players, ImprovedShooting shoot, IUT2004Navigation navigation)
    {
        super(players, shoot, navigation);
    }

    public void BehaviourDrivenMovement()
    {
        Player target = getPlayers().getNearestVisibleEnemy();
        navigation.navigate(target);
        shoot.shoot(target);
    }

}
