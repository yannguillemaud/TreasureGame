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
        if(tokens.length == 0) throw new IllegalArgumentException("Incorrect parsing values");

        var charCode = tokens[0];
        return Optional.ofNullable(switch (charCode) {

            case "A" -> {
                if(tokens.length < 5) throw new IllegalArgumentException("Not enough playable parameters");
                var name = tokens[1];
                var x = Integer.parseInt(tokens[2]);
                var y = Integer.parseInt(tokens[3]);
                var orientation = Orientation.fromString(tokens[4]);
                var sequence = tokens.length == 6 ? tokens[5] : "";

                yield new BasicPlayer.PlayerBuilder()
                        .withName(name)
                        .withPosition(new Position(x, y))
                        .withOrientation(orientation)
                        .withSequence(sequence)
                        .build();
            }

            case "T" -> {
                if(tokens.length < 4) throw new IllegalArgumentException("Not enough treasure parameters");
                var x = Integer.parseInt(tokens[1]);
                var y = Integer.parseInt(tokens[2]);
                var nbTrasures = Integer.parseInt(tokens[3]);
                yield new Treasure(new Position(x, y), nbTrasures);
            }

            case "M" -> {
                if(tokens.length < 3) throw new IllegalArgumentException("Not enough Mountain parameters");
                var x = Integer.parseInt(tokens[1]);
                var y = Integer.parseInt(tokens[2]);
                yield new Mountain(new Position(x, y));
            }

            default -> null;
        });
    }
}
