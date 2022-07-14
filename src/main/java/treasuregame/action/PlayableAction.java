package treasuregame.action;

import treasuregame.action.IAction;
import treasuregame.component.GameComponent;

public interface PlayableAction extends IAction {
    enum AuthorizedActions {
        MOVE_UP,
        MOVE_LEFT,
        MOVE_RIGHT
    }

    @Override
    void apply(GameComponent component);
}
