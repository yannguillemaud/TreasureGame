package treasuregame.visitor.action;

import treasuregame.action.IAction;
import treasuregame.action.MoveAction;
import treasuregame.action.TurnAction;
import treasuregame.component.playable.Playable;

import java.util.Objects;

/**
 * Visitor used to retrieve data among Playable's movement
 */
public interface MovementActionVisitor {
    void applyMovement(MoveAction action, Playable playable);
    void applyMovement(TurnAction action, Playable playable);

    default void applyMovement(IAction action, Playable playable){
        Objects.requireNonNull(action);
        Objects.requireNonNull(playable);

        switch (action){
            case MoveAction moveAction -> applyMovement(moveAction, playable);
            case TurnAction turnAction -> applyMovement(turnAction, playable);
            default -> {}
        }
    }
}
