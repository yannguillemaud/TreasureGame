package treasuregame.component;

import treasuregame.action.IAction;

/**
 * Interface used to describe every component of a game
 * V1.1
 */
public interface GameComponent {
    void applyAction(IAction action);

    void updatePosition(Position newPosition);
    Position getPosition();
}
