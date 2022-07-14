package treasuregame.factory.component;

import treasuregame.component.*;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.playable.Orientation;

import java.util.Objects;
import java.util.Optional;

/**
 * Class used to create GameComponent from String lines
 * V1.0
 * Might be refactored if enough time left
 */
public class StringComponentFactory implements GameComponentFactory<String> {
    @Override
    public Optional<GameComponent> fromSource(String source) {
        Objects.requireNonNull(source);
        var tokens = source.split(" - ");
        var charCode = tokens[0];
        return Optional.ofNullable(switch (charCode) {
            case "A" -> {
                if(tokens.length < 6) throw new IllegalArgumentException();
                var name = tokens[1];
                var x = Integer.parseInt(tokens[2]);
                var y = Integer.parseInt(tokens[3]);
                var position = new Position(x, y);
                var orientation = Orientation.fromString(tokens[4]);
                var sequence = tokens[5];
                yield new BasicPlayer.PlayerBuilder()
                        .withName(name)
                        .withPosition(position)
                        .withOrientation(orientation)
                        .withSequence(sequence)
                        .build();
            }
            case "T" -> {
                if(tokens.length < 4) throw new IllegalArgumentException();
                var x = Integer.parseInt(tokens[1]);
                var y = Integer.parseInt(tokens[2]);
                var position = new Position(x, y);
                var nbTrasure = Integer.parseInt(tokens[3]);
                yield new Treasure(position, nbTrasure);
            }
            case "M" -> {
                if(tokens.length < 3) throw new IllegalArgumentException();
                var x = Integer.parseInt(tokens[1]);
                var y = Integer.parseInt(tokens[2]);
                var position = new Position(x, y);
                yield new Mountain(position);
            }
            default -> null;
        });
    }
}
