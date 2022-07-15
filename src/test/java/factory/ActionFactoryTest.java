package factory;

import org.junit.jupiter.api.Test;
import treasuregame.action.MoveAction;
import treasuregame.action.TurnAction;
import treasuregame.component.playable.Orientation;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.Position;
import treasuregame.factory.action.CharActionFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ActionFactoryTest {
    private static final CharActionFactory factory = new CharActionFactory();

    @Test
    public void shouldThrowNPE(){
        assertThrows(NullPointerException.class, () -> factory.fromSource(null));
    }

    @Test
    public void shouldCreateMoveAction(){
        var optionalAction = factory.fromSource('A');
        assertDoesNotThrow(() -> assertTrue(optionalAction.orElseThrow() instanceof MoveAction));
    }

    @Test
    public void shouldPlayerMoveLeft(){
        var moveAction = factory.fromSource('A');
        var player = new BasicPlayer.PlayerBuilder()
                .withName("Alice")
                .withPosition(new Position(0, 0))
                .withOrientation(Orientation.SOUTH)
                .build();

        assertDoesNotThrow(() -> {
            player.applyAction(moveAction.orElseThrow());
            assertEquals(new Position(0, 1), player.getPosition());
        });
    }

    @Test
    public void shouldCreateTurnAction(){
        var optionalLeft = factory.fromSource('G');
        var optionalRight = factory.fromSource('D');

        assertDoesNotThrow(
            () -> {
                assertEquals(TurnAction.class, optionalLeft.orElseThrow().getClass());
                assertEquals(TurnAction.class, optionalRight.orElseThrow().getClass());
            }
        );
    }

    @Test
    public void playerShouldTurn(){
        var optionalLeft = factory.fromSource('G');
        var optionalRight = factory.fromSource('D');

        var player = new BasicPlayer.PlayerBuilder()
                .withName("Alice")
                .withPosition(new Position(0, 0))
                .withOrientation(Orientation.NORTH)
                .build();

        assertDoesNotThrow(() -> {
            var left = optionalLeft.orElseThrow();
            var right = optionalRight.orElseThrow();

            player.applyAction(left);
            assertEquals(Orientation.WEST, player.getOrientation());
            player.applyAction(right);
            player.applyAction(right);
            assertEquals(Orientation.EAST, player.getOrientation());
        });
    }
}
