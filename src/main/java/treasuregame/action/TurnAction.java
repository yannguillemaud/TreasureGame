package treasuregame.action;

import treasuregame.component.GameComponent;
import treasuregame.component.playable.Orientation;
import treasuregame.component.playable.Playable;

import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class TurnAction implements PlayableAction {
    private final Function<Playable, Orientation> orientationFunction;

    public TurnAction(Function<Playable, Orientation> orientationFunction){
        this.orientationFunction = requireNonNull(orientationFunction);
    }

    @Override
    public void apply(GameComponent component) {
        requireNonNull(component);
        if(component instanceof Playable playable) {
            playable.updateOrientation(orientationFunction.apply(playable));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnAction that = (TurnAction) o;
        return Objects.equals(orientationFunction, that.orientationFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientationFunction);
    }

    @Override
    public String toString() {
        return "TurnAction";
    }

}
