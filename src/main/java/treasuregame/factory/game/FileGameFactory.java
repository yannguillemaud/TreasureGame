package treasuregame.factory.game;

import treasuregame.factory.component.GameComponentFactory;
import treasuregame.game.BasicGameImpl;
import treasuregame.component.IGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

/**
 * Class used to create Games through String lines parsing (time prioritization)
 * It uses a GameComponentFactory<String> for component creation
 * V1.0
 */
public class FileGameFactory implements GameFactory<Path> {
    private final GameComponentFactory<String> mapper;

    public FileGameFactory(GameComponentFactory<String> mapper){
        this.mapper = Objects.requireNonNull(mapper);
    }

    private BasicGameImpl createGame(String source){
        var tokens = source.split(" - ");
        if(!tokens[0].equals("C")) throw new IllegalArgumentException("Game line: incorrect values");
        return new BasicGameImpl(Integer.parseInt(tokens[1]) -1, Integer.parseInt(tokens[2]) -1);
    }

    @Override
    public Optional<IGame> fromSource(Path path) {
        Objects.requireNonNull(path);
        if(!Files.exists(path)) throw new IllegalArgumentException();
        try {
            var content = Files.readAllLines(path);
            // Time prioritization for game creation
            var game = content.stream()
                    .findFirst()
                    .map(this::createGame)
                    .orElseThrow(() -> new IllegalArgumentException("Game line not found"));
            // Components creation
            content.stream()
                    .skip(1)
                    .map(mapper::fromSource)
                    .forEach(optional -> optional.ifPresent(game::registerGameComponent));
            return Optional.of(game);
        } catch (IOException e){
            throw new AssertionError(e);
        }
    }
}
