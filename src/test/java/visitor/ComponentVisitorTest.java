package visitor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import treasuregame.component.GameComponent;
import treasuregame.component.Position;
import treasuregame.factory.component.GameComponentFactory;
import treasuregame.factory.component.StringComponentFactory;
import treasuregame.factory.game.FileGameFactory;
import treasuregame.factory.game.GameFactory;
import treasuregame.game.BasicGameImpl;
import treasuregame.visitor.component.ComponentToStringVisitor;
import treasuregame.visitor.component.GameComponentVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComponentVisitorTest {
    private static final GameComponentFactory<String> COMPONENT_FACTORY = new StringComponentFactory();
    private static final GameFactory<Path> GAME_FACTORY = new FileGameFactory(COMPONENT_FACTORY);
    private static final String USE_CASE = """
            C - 3 - 4
            M - 1 - 0
            M - 2 - 1
            T - 0 - 3 - 2
            T - 1 - 3 - 3
            """;

    private static final String PLAYERS_CASE = """
            C - 3 - 4
            A - Alice - 1 - 1 - S
            A - Bob - 1 - 2 - N - AA
            """;

    private static final ComponentToStringVisitor STRING_VISITOR = new ComponentToStringVisitor();

    @Test
    public void shouldGetCorrectPositions(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, USE_CASE.getBytes());
        BasicGameImpl game = (BasicGameImpl) GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.runGame();

        var positions = game.getComponents().stream()
                .map(GameComponent::getPosition)
                .toList();

        assertTrue(List.of(
                new Position(1, 0),
                new Position(2, 1),
                new Position(0, 3),
                new Position(1, 3))
                .containsAll(positions)
        );
    }

    @Test
    public void shouldGetCorrectPositions2(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, PLAYERS_CASE.getBytes());
        BasicGameImpl game = (BasicGameImpl) GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.runGame();

        var positions = game.getComponents().stream()
                .map(GameComponent::getPosition)
                .toList();

        assertTrue(List.of(
                        new Position(1, 1),
                        new Position(1, 2))
                .containsAll(positions)
        );
    }

    @Test
    public void shouldGetCorrectStrings(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, USE_CASE.getBytes());
        BasicGameImpl game = (BasicGameImpl) GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.runGame();

        var strings = game.getComponents().stream()
                .map(STRING_VISITOR::visit)
                .map(StringBuilder::toString)
                .toList();

        assertTrue(List.of(
                "M - 1 - 0",
                "M - 2 - 1",
                "T - 0 - 3 - 2",
                "T - 1 - 3 - 3" )
                .containsAll(strings)
        );
    }
}
