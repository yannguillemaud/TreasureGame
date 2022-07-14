package factory;

import org.junit.jupiter.api.Test;
import treasuregame.component.*;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.playable.Orientation;
import treasuregame.factory.game.FileGameFactory;
import treasuregame.factory.component.GameComponentFactory;
import treasuregame.factory.component.StringComponentFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameComponentFactoryTest {
    private static final GameComponentFactory<String> factory = new StringComponentFactory();

    @Test
    public void creationShouldThrowNPE(){
        assertThrows(NullPointerException.class, () -> new FileGameFactory(null));
    }

    @Test
    public void creationShouldCreateMountain(){
        var optionalMountain = factory.fromSource("M - 1 - 1");
        assertAll(
                () -> assertDoesNotThrow(optionalMountain::get),
                () -> assertEquals(optionalMountain.get(), new Mountain(new Position(1, 1)))
        );
    }

    @Test
    public void creationShouldCreateTreasure(){
        var optionalTreasure = factory.fromSource("T - 1 - 1 - 2");
        assertAll(
                () -> assertDoesNotThrow(optionalTreasure::get),
                () -> assertEquals(optionalTreasure.get(), new Treasure(new Position(1, 1), 2))
        );
    }

    @Test
    public void creationShouldCreatePlayer(){
        var optionalPlayer = factory.fromSource("A - Alice - 1 - 1 - S - AA");
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
    public void playerShouldNotBeCreated(){
        assertThrows(IllegalArgumentException.class, () -> factory.fromSource("A - 1 - S - AA"));
    }

    @Test
    public void factoryShouldNotCreateComponent(){
        assertEquals(factory.fromSource("# commentary "), Optional.empty());
    }
}
