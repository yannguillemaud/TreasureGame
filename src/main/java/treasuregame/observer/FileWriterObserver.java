package treasuregame.observer;

import treasuregame.game.BasicGameImpl;
import treasuregame.visitor.component.ComponentToStringVisitor;
import treasuregame.visitor.component.GameComponentVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class FileWriterObserver implements BasicGameObserver {
    private static final GameComponentVisitor<String> TO_STRING_VISITOR = new ComponentToStringVisitor();
    private static final String FILE_NAME = "output.txt";

    private final Path directory;

    public FileWriterObserver(Path directory){
        this.directory = Objects.requireNonNull(directory);
    }

    public FileWriterObserver(){
        this(Path.of(""));
    }

    @Override
    public void onGameStop(BasicGameImpl game) {
        var gameResult = game.getComponents().stream()
                .map(TO_STRING_VISITOR::visit)
                .filter(not(String::isBlank))
                .collect(Collectors.joining("\n", game.getSizeMap().concat("\n"), ""));

        try {
            var path = directory.resolve(FILE_NAME);
            Files.deleteIfExists(path);
            var output = Files.createFile(path);
            Files.write(output, gameResult.getBytes());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
