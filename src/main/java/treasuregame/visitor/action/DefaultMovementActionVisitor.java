package treasuregame.visitor.action;

import treasuregame.action.MoveAction;
import treasuregame.action.TurnAction;
import treasuregame.component.playable.Playable;
import treasuregame.visitor.component.TreasureCollisionVisitor;
import treasuregame.component.Map;

import java.util.Objects;

/**
 * Visitor used to perform default movements of a player
 * Current movements atm:
 * - Move
 * - Turn
 */
public class DefaultMovementActionVisitor implements MovementActionVisitor {
    private final Map map;

    public DefaultMovementActionVisitor(Map map) {
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public void applyMovement(MoveAction action, Playable playable) {
        var moveFunction = action.getOperator();
        var nextPosition = moveFunction.apply(playable);

        // if the movement is authorized, move the player and apply a collision
        if(map.isAvailable(playable, nextPosition)){
            playable.applyAction(action);
            map.applyOnComponentsOnCase(playable.getPosition(), new TreasureCollisionVisitor(playable));
        }
    }

    @Override
    public void applyMovement(TurnAction action, Playable playable) {
        playable.applyAction(action);
    }
}
