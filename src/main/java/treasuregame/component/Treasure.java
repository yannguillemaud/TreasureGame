package treasuregame.component;

import treasuregame.action.IAction;

import java.util.Objects;

/**
 * Class used to represent a game Treasure
 */
public class Treasure implements GameComponent {
    private Position position;
    private int remainingTreasure;

    public Treasure(Position position, int remainingTreasure){
        this.position = position;
        this.remainingTreasure = remainingTreasure;
    }

    @Override
    public void applyAction(IAction action) {
        action.apply(this);
    }

    @Override
    public void updatePosition(Position newPosition) {
        this.position = newPosition;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void findTreasure(int nb){
        remainingTreasure = remainingTreasure - nb;
    }

    public int getRemainingTreasure() {
        return remainingTreasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treasure treasure = (Treasure) o;
        return remainingTreasure == treasure.remainingTreasure && Objects.equals(position, treasure.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, remainingTreasure);
    }

    @Override
    public String toString() {
        return "Treasure{" +
                "position=" + position +
                ", remainingTreasure=" + remainingTreasure +
                '}';
    }
}
