package treasuregame.component.playable;

import treasuregame.action.IAction;
import treasuregame.component.GameComponent;

import java.util.Optional;

/**
 * Interface used to define player with orientation and actions
 */
public interface Playable extends GameComponent {
    void updateOrientation(Orientation orientation);
    Optional<IAction> acquireNextAction();
    Orientation getOrientation();
}
