package treasuregame.factory.action;

import treasuregame.action.IAction;
import treasuregame.action.MoveAction;
import treasuregame.action.PlayableAction;
import treasuregame.action.TurnAction;
import treasuregame.component.playable.Orientation;
import treasuregame.component.Position;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static treasuregame.component.playable.Orientation.*;
import static treasuregame.action.PlayableAction.*;
import static treasuregame.action.PlayableAction.AuthorizedActions.*;

/**
 * Factory used to create IAction from Character
 * Current Actions:
 * A - MoveUp => MoveAction
 * G, D - MoveLeft => TurnAction
 */
public class CharActionFactory implements ActionFactory<Character> {
    private static final Map<AuthorizedActions, PlayableAction> ACTION_MAP = Map.of(
        MOVE_UP, new MoveAction(component -> {
            var position = component.getPosition();
                return switch (component.getOrientation()) {
                    case NORTH -> new Position(position.x(), position.y() - 1);
                    case SOUTH -> new Position(position.x(), position.y() + 1);
                    case EAST -> new Position(position.x() + 1, position.y());
                    case WEST -> new Position(position.x() - 1, position.y());
                };
        }),

        MOVE_LEFT, new TurnAction(component -> switch(component.getOrientation()) {
            case NORTH -> Orientation.WEST;
            case SOUTH -> Orientation.EAST;
            case EAST -> NORTH;
            case WEST -> Orientation.SOUTH;
        }),

        MOVE_RIGHT, new TurnAction(component -> switch(component.getOrientation()) {
            case NORTH -> Orientation.EAST;
            case SOUTH -> Orientation.WEST;
            case EAST -> Orientation.SOUTH;
            case WEST -> NORTH;
        })
    );

    @Override
    public Optional<IAction> fromSource(Character source) {
        Objects.requireNonNull(source);
        return Optional.ofNullable(switch (source){
            case 'A' -> ACTION_MAP.get(MOVE_UP);
            case 'G' -> ACTION_MAP.get(MOVE_LEFT);
            case 'D' -> ACTION_MAP.get(MOVE_RIGHT);
            default -> null;
        });
    }
}
