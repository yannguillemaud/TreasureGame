package treasuregame.action;

import treasuregame.component.GameComponent;
import treasuregame.component.playable.Playable;
import treasuregame.component.Position;

import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class MoveAction implements PlayableAction {

    private final Function<Playable, Position> positionFunction;

    public MoveAction(Function<Playable, Position> unaryOperator){
        this.positionFunction = requireNonNull(unaryOperator);
    }
    @Override
    public void apply(GameComponent component) {
        requireNonNull(component);
        if(component instanceof Playable playable) {
            component.updatePosition(positionFunction.apply(playable));
        }
    }

    public Function<Playable, Position> getOperator() {
        return positionFunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveAction that = (MoveAction) o;
        return Objects.equals(positionFunction, that.positionFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionFunction);
    }

    @Override
    public String toString() {
        return "MoveAction";
    }
}
