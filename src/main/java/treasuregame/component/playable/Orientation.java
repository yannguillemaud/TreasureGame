package treasuregame.component.playable;

/**
 * Enumeration representing the 4 different orientations
 */
public enum Orientation {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W");

    private final String code;
    Orientation(String code){
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static Orientation fromString(String code){
        return switch (code){
            case "N" -> NORTH;
            case "S" -> SOUTH;
            case "E" -> EAST;
            case "W" -> WEST;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }
}
