
package Univale.Tcc.RL.Pogamut.Services;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import Univale.Tcc.RL.Pogamut.Actions.ActionEngage;
import Univale.Tcc.RL.Pogamut.Actions.ActionNavPoint;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState;
import cz.cuni.amis.pogamut.base.utils.math.DistanceUtils;
import cz.cuni.amis.pogamut.base3d.worldview.IVisionWorldView;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.Players;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author winicius
 */
public class GameStateFactory {

    protected Players players;
    protected AgentInfo agentInfo;
    protected NavPoint currentPosition;

    public GameStateFactory(Players players, AgentInfo agentInfo)
    {
        this.players = players;
        this.agentInfo = agentInfo;
        currentPosition = agentInfo.getNearestNavPoint();
    }
    public GameState getGameState(IVisionWorldView wordView)
    {

        GameState result = new GameState();
        result.setPreviousPosition(currentPosition.getId());
        result.setEnemiesCount(players.getVisibleEnemies().size());
        result.setHealthLow(agentInfo.getHealth() < 60);

        NavPoint navPoint = agentInfo.getNearestNavPoint();
        result.setNavPoint(navPoint.getId());
        currentPosition = navPoint;

        List<Action> actions = navPoint.getOutgoingEdges().values().
                stream().map(nav -> new ActionNavPoint(nav.getToNavPoint().getId()))
                .collect(Collectors.toList());

        if(players.canSeeEnemies()) {
            Action actionEngage = new ActionEngage();
            actionEngage.setQValue(0.1f);
            actions.add(actionEngage);

            Location location = players.getNearestEnemy(2000).getLocation();
            NavPoint enemyNav = DistanceUtils.getNearest(wordView.getAll(NavPoint.class).values(), location);
            result.setNearestEnemyPosition(enemyNav.getId());
        }
        result.setAvailableActions(actions);
        return result;
    }
}
