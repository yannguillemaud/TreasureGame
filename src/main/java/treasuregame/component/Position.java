package treasuregame.component;

/**
 * Record used to represent a specific position on a game
 * @param x
 * @param y
 */
public record Position(int x, int y) {
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}