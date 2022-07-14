package treasuregame.action;

import treasuregame.component.GameComponent;
import treasuregame.component.playable.BasicPlayer;

import java.util.Objects;

public class PickTreasureAction implements IAction {
    @Override
    public void apply(GameComponent component) {
        Objects.requireNonNull(component);
        if (component instanceof BasicPlayer basicPlayer) {
            basicPlayer.pickTreasure();
        }
    }
}
