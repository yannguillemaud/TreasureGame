package treasuregame.component.playable;

import treasuregame.action.IAction;
import treasuregame.component.Position;
import treasuregame.factory.action.CharActionFactory;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class BasicPlayer implements Playable {
    public static class PlayerBuilder {
        private Position position;
        private String name;
        private String sequence;
        private Orientation orientation;

        public PlayerBuilder withPosition(Position position){ this.position = position; return this; }
        public PlayerBuilder withName(String name) { this.name = name; return this; }
        public PlayerBuilder withSequence(String sequence) { this.sequence = sequence; return this; }
        public PlayerBuilder withOrientation(Orientation orientation){ this.orientation = orientation; return this; }
        public BasicPlayer build(){ return new BasicPlayer(this); }
    }
    private Position position;
    private final String name;
    private Orientation orientation;
    private Queue<IAction> sequence = new ArrayDeque<>();
    private static final CharActionFactory factory = new CharActionFactory();
    private int foundTreasures = 0;

    private BasicPlayer(PlayerBuilder builder) {
        this.position = builder.position;
        this.name = builder.name;
        this.orientation = builder.orientation;
        if(builder.sequence != null) {
            this.sequence = builder.sequence
                    .codePoints()
                    .mapToObj(c -> (char) c)
                    .map(factory::fromSource)
                    .filter(Predicate.not(Optional::isEmpty))
                    .map(Optional::get)
                    .collect(Collectors.toCollection(ArrayDeque::new));
        }
    }

    public void pickTreasure(){
        foundTreasures++;
    }

    @Override
    public void applyAction(IAction action) {
        requireNonNull(action);
        action.apply(this);
    }

    @Override
    public void updatePosition(Position newPosition) { this.position = requireNonNull(newPosition); }

    @Override
    public void updateOrientation(Orientation orientation) {
        this.orientation = requireNonNull(orientation);
    }

    public Optional<IAction> acquireNextAction(){
        return Optional.ofNullable(sequence.poll());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPlayer that = (BasicPlayer) o;
        return foundTreasures == that.foundTreasures && Objects.equals(position, that.position) && Objects.equals(name, that.name) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, name, orientation, foundTreasures);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    public String getName() {
        return name;
    }

    public int getFoundTreasures() {
        return foundTreasures;
    }


}
