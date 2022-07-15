package factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import treasuregame.component.*;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.playable.Orientation;
import treasuregame.factory.game.FileGameFactory;
import treasuregame.factory.component.GameComponentFactory;
import treasuregame.factory.component.StringComponentFactory;
import treasuregame.factory.game.GameFactory;
import treasuregame.game.BasicGameImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameComponentFactoryTest {
    private static final GameComponentFactory<String> STRING_COMPONENT_FACTORY = new StringComponentFactory();
    private static final GameFactory<Path> GAME_FACTORY = new FileGameFactory(STRING_COMPONENT_FACTORY);
    private static final String USE_CASE = """
            C - 3 - 4
            M - 1 - 0
            M - 2 - 1
            T - 0 - 3 - 2
            T - 1 - 3 - 3
            """;

    @Test
    public void creationShouldThrowNPE(){
        assertThrows(NullPointerException.class, () -> new FileGameFactory(null));
    }

    @Test
    public void creationShouldCreateMountain(){
        var optionalMountain = STRING_COMPONENT_FACTORY.fromSource("M - 1 - 1");
        assertDoesNotThrow(
                () -> assertEquals(optionalMountain.orElseThrow(), new Mountain(new Position(1, 1)))
        );
    }

    @Test
    public void creationShouldCreateTreasure(){
        var optionalTreasure = STRING_COMPONENT_FACTORY.fromSource("T - 1 - 1 - 2");
        assertDoesNotThrow(
                () -> assertEquals(optionalTreasure.orElseThrow(), new Treasure(new Position(1, 1), 2))
        );
    }

    @Test
    public void creationShouldThrowErrorWhenCreatePlayer(){
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new BasicPlayer.PlayerBuilder()
                                .withPosition(new Position(1, 1))
                                .withOrientation(Orientation.SOUTH)
                                .withSequence("AA")
                                .build()),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new BasicPlayer.PlayerBuilder()
                                .withName("Lara")
                                .withOrientation(Orientation.SOUTH)
                                .withSequence("AA")
                                .build()),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        new BasicPlayer.PlayerBuilder()
                                .withName("Lara")
                                .withPosition(new Position(1, 1))
                                .withSequence("AA")
                                .build()),
                () -> assertDoesNotThrow(() ->
                        new BasicPlayer.PlayerBuilder()
                                .withName("Lara")
                                .withPosition(new Position(1, 1))
                                .withOrientation(Orientation.SOUTH)
                                .build())
        );

    }

    @Test
    public void creationShouldCreatePlayer(){
        var optionalPlayer = STRING_COMPONENT_FACTORY.fromSource("A - Alice - 1 - 1 - S - AA");
        assertDoesNotThrow(() -> assertEquals(
                new BasicPlayer.PlayerBuilder()
                    .withName("Alice")
                    .withPosition(new Position(1, 1))
                    .withSequence("AA")
                    .withOrientation(Orientation.SOUTH)
                    .build(),
                optionalPlayer.orElseThrow()));
    }

    @Test
    public void creationShouldCreatePlayer2(){
        var optionalPlayer = STRING_COMPONENT_FACTORY.fromSource("A - Alice - 1 - 1 - S - ");
        assertDoesNotThrow(() -> assertEquals(
                new BasicPlayer.PlayerBuilder()
                        .withName("Alice")
                        .withPosition(new Position(1, 1))
                        .withOrientation(Orientation.SOUTH)
                        .build(),
                optionalPlayer.orElseThrow()));
    }

    @Test
    public void playerShouldNotBeCreated(){
        assertThrows(IllegalArgumentException.class, () -> STRING_COMPONENT_FACTORY.fromSource("A - 1 - S - AA"));
    }

    @Test
    public void factoryShouldNotCreateComponent(){
        assertEquals(STRING_COMPONENT_FACTORY.fromSource("# commentary "), Optional.empty());
    }

    @Test
    public void shouldReturnComponentPosition(@TempDir Path tempDir) throws IOException {
        var inputPath = Files.createTempFile(tempDir, "input", "txt");
        Files.write(inputPath, USE_CASE.getBytes());
        BasicGameImpl game = (BasicGameImpl) GAME_FACTORY.fromSource(inputPath).orElseThrow();
        game.runGame();

        System.out.println(game.getComponents());
        assertTrue(
                List.of(
                        new Mountain(new Position(1, 0)),
                        new Mountain(new Position(2, 1)),
                        new Treasure(new Position(0, 3), 2),
                        new Treasure(new Position(1, 3), 3),
                        new BasicPlayer.PlayerBuilder()
                                .withName("Lara")
                                .withPosition(new Position(1, 1))
                                .withOrientation(Orientation.SOUTH)
                                .withSequence("AADADAGGA")
                                .build()
                ).containsAll(game.getComponents())
        );
    }
}
