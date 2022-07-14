package treasuregame.visitor.component;

import treasuregame.component.Mountain;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.Position;
import treasuregame.component.Treasure;

public class PositionVisitor implements GameComponentVisitor<Position> {
    @Override
    public Position visit(BasicPlayer playable) {
        playable.updatePosition(playable.getPosition());
        return playable.getPosition();
    }

    @Override
    public Position visit(Mountain mountain) {
        mountain.updatePosition(mountain.getPosition());
        return mountain.getPosition();
    }

    @Override
    public Position visit(Treasure treasure) {
        treasure.updatePosition(treasure.getPosition());
        return treasure.getPosition();
    }
}
