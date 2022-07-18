package treasuregame.visitor.component;

import treasuregame.action.IAction;
import treasuregame.component.GameComponent;
import treasuregame.component.Mountain;
import treasuregame.component.Treasure;
import treasuregame.component.playable.BasicPlayer;

import java.util.Objects;

/**
 * Class used to apply a collision between a player and a treasure
 */
public class TreasureCollisionVisitor implements GameComponentVisitor<Void> {
    private static final IAction pickTreasureAction = component -> {
        if (component instanceof BasicPlayer basicPlayer) {
            basicPlayer.pickTreasure();
        }
    };

    private final GameComponent player;

    public TreasureCollisionVisitor(GameComponent player){
        this.player = Objects.requireNonNull(player);
    }

    @Override
    public Void visit(BasicPlayer playable) {
        return null;
    }

    @Override
    public Void visit(Mountain mountain) {
        return null;
    }

    @Override
    public Void visit(Treasure treasure) {
        player.applyAction(pickTreasureAction);
        treasure.findTreasure(1);
        return null;
    }
}
