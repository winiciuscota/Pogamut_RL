package Univale.Tcc.RL.Pogamut.Behaviors.Abstract;

import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.Players;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.IUT2004Navigation;
import cz.cuni.amis.pogamut.ut2004.bot.command.ImprovedShooting;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * Created by winicius on 10/11/2014.
 */
public class Behavior {
    private Players players;
    protected ImprovedShooting shoot;
    protected IUT2004Navigation navigation;

    public Behavior(Players players, ImprovedShooting shoot, IUT2004Navigation navigation)
    {
        setShoot(shoot);
        setPlayers(players);
        setNavigation(navigation);
    }

    public ImprovedShooting getShoot() {
        return shoot;
    }

    public void setShoot(ImprovedShooting shoot) {
        this.shoot = shoot;
    }

    public IUT2004Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(IUT2004Navigation navigation) {
        this.navigation = navigation;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }
}
