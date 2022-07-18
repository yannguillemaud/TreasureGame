package basicgame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import treasuregame.component.Mountain;
import treasuregame.component.Position;
import treasuregame.component.Treasure;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.playable.Orientation;
import treasuregame.factory.component.GameComponentFactory;
import treasuregame.factory.component.StringComponentFactory;
import treasuregame.factory.game.FileGameFactory;
import treasuregame.factory.game.GameFactory;
import treasuregame.game.BasicGameImpl;
import treasuregame.component.IGame;
import treasuregame.observer.FileWriterObserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGameTest {
    private static final GameComponentFactory<String> COMPONENT_FACTORY = new StringComponentFactory();
    private static final GameFactory<Path> GAME_FACTORY = new FileGameFactory(COMPONENT_FACTORY);
    private static final String OUTPUT_NAME = "output.txt";
    private static final String START_USECASE = """
            C - 3 - 4
            M - 1 - 0
            M - 2 - 1
            T - 0 - 3 - 2
            T - 1 - 3 - 3
            A - Lara - 1 - 1 - S - AADADAGGA
            """;

    private static final String END_USECASE = """
            C - 3 - 4
            M - 1 - 0
            M - 2 - 1
            T - 1 - 3 - 2
            A - Lara - 0 - 3 - S - 3
            """.strip();

    private static final String START_BLOCKING_MAP = """
            C - 3 - 4
            A - Lara - 1 - 1 - S - AAAAAA
            """;

    private static final String END_BLOCKING_MAP = """
            C - 3 - 4
            A - Lara - 1 - 3 - S - 0
            """.strip();

    private static final String START_BLOCKING_MOUNTAIN = """
            C - 3 - 4
            M - 1 - 3
            A - Lara - 1 - 1 - S - AAA
            """;

    private static final String END_BLOCKING_MOUNTAIN = """
            C - 3 - 4
            M - 1 - 3
            A - Lara - 1 - 2 - S - 0
            """.strip();

    private static final String START_FIND_TREASURE = """
            C - 3 - 4
            T - 1 - 2 - 2
            A - Lara - 1 - 1 - S - A
            """;

    private static final String END_FIND_TREASURE = """
            C - 3 - 4
            T - 1 - 2 - 1
            A - Lara - 1 - 2 - S - 1
            """.strip();

    private static final String START_FAIL = """
            C - 3 - 4
            M - 1 - 1 
            A - Lara - 1 - 1 - S - A
            """;


    @Test
    public void registerShouldThrowIAE(){
        var lambdaMountain = new Mountain(new Position(1, 0));
        var otherMountain = new Mountain(new Position(1, 1));

        IGame game = new BasicGameImpl(1, 1);
        game.registerGameComponent(lambdaMountain);

        assertAll(
                () -> assertThrows(NullPointerException.class, () -> game.registerGameComponent(null)),
                () -> assertThrows(NullPointerException.class, () -> game.unregisterGameComponent(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> game.unregisterGameComponent(otherMountain)),
                () -> assertDoesNotThrow(() -> game.unregisterGameComponent(lambdaMountain))
        );
    }

    @Test
    public void runGameShouldThrowIAEWithIncorrectMapCoords(){
        var game1 = new BasicGameImpl(-1, 0);
        var game2 = new BasicGameImpl(0, -1);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, game1::runGame),
                () -> assertThrows(IllegalArgumentException.class, game2::runGame)
        );
    }

    @Test
    public void runGameShouldThrowIAEWithIncorrectComponentsCoords() {
        var game = new BasicGameImpl(3, 3);
        game.registerGameComponent(new Mountain(new Position(4, 3)));
        assertThrows(IllegalArgumentException.class, game::runGame);
    }

    @Test
    public void runGameShouldThrowIAEWithIncorrectComponentsCoords2() {
        var game = new BasicGameImpl(3, 3);
        game.registerGameComponent(
                new BasicPlayer.PlayerBuilder()
                        .withName("Alice")
                        .withPosition(new Position(3, 3))
                        .withOrientation(Orientation.SOUTH)
                        .build()
        );
        game.registerGameComponent(
                new BasicPlayer.PlayerBuilder()
                        .withName("Bob")
                        .withPosition(new Position(3, 3))
                        .withOrientation(Orientation.NORTH)
                        .build()
        );
        assertThrows(IllegalArgumentException.class, game::runGame);
    }

    @Test
    public void runGameShouldThrowIAEWithIncorrectComponentsCoords3() {
        var game = new BasicGameImpl(3, 3);
        game.registerGameComponent(
                new BasicPlayer.PlayerBuilder()
                        .withName("Alice")
                        .withPosition(new Position(3, 3))
                        .withOrientation(Orientation.SOUTH)
                        .build()
        );
        game.registerGameComponent(new Mountain(new Position(3, 3)));
        assertThrows(IllegalArgumentException.class, game::runGame);
    }

    @Test
    public void runGameShouldThrowIAEWithIncorrectComponentsCoords4() {
        var game = new BasicGameImpl(3, 3);
        game.registerGameComponent(new Treasure(new Position(3, 3), 1));
        game.registerGameComponent(
                new BasicPlayer.PlayerBuilder()
                        .withName("Alice")
                        .withPosition(new Position(3, 3))
                        .withOrientation(Orientation.SOUTH)
                        .build()
        );
        assertDoesNotThrow(game::runGame);
    }

    @Test
    public void runShouldCreateOutput(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_USECASE.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.registerObserver(new FileWriterObserver(tempDir));
        game.runGame();
        assertEquals(true, Files.exists(tempDir.resolve(OUTPUT_NAME)));
    }

    @Test
    public void runShouldCreateCorrectOutput(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_USECASE.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.registerObserver(new FileWriterObserver(tempDir));
        game.runGame();
        assertEquals(
                END_USECASE,
                Files.readString(tempDir.resolve(OUTPUT_NAME))
        );
    }

    @Test
    public void playerShouldBeBlockedByMap (@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_BLOCKING_MAP.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.registerObserver(new FileWriterObserver(tempDir));
        game.runGame();
        assertEquals(
                END_BLOCKING_MAP,
                Files.readString(tempDir.resolve(OUTPUT_NAME))
        );
    }

    @Test
    public void playerShouldBeBlockedByMountain (@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_BLOCKING_MOUNTAIN.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.registerObserver(new FileWriterObserver(tempDir));
        game.runGame();
        assertEquals(
                END_BLOCKING_MOUNTAIN,
                Files.readString(tempDir.resolve(OUTPUT_NAME))
        );
    }

    @Test
    public void playerShouldFindTreasures (@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_FIND_TREASURE.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.registerObserver(new FileWriterObserver(tempDir));
        game.runGame();
        assertEquals(
                END_FIND_TREASURE,
                Files.readString(tempDir.resolve(OUTPUT_NAME))
        );
    }

    @Test
    public void gameShouldNotRun(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, START_FAIL.getBytes());
        var game = GAME_FACTORY.fromSource(inputPath).orElseThrow();
        assertThrows(
                IllegalArgumentException.class,
                game::runGame
        );
    }
}
