package treasuregame.component;

import treasuregame.action.IAction;

import java.util.Objects;

/**
 * Represents a blocking mountain
 */
public class Mountain implements GameComponent {
    private Position position;

    public Mountain(Position position){
        this.position = position;
    }

    @Override
    public void updatePosition(Position newPosition) {
        this.position = newPosition;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void applyAction(IAction action) {
        action.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mountain mountain = (Mountain) o;
        return Objects.equals(position, mountain.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Mountain{" +
                "position=" + position +
                '}';
    }
}
