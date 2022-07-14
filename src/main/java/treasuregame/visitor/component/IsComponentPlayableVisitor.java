package treasuregame.visitor.component;

import treasuregame.component.Mountain;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.Treasure;

import java.util.Optional;

/**
 * Visitor used to retrieve players among different GameComponents
 * instanceof ?
 */
public class IsComponentPlayableVisitor implements GameComponentVisitor<Optional<BasicPlayer>>{
    @Override
    public Optional<BasicPlayer> visit(BasicPlayer playable) {
        return Optional.of(playable);
    }

    @Override
    public Optional<BasicPlayer> visit(Mountain mountain) {
        return Optional.empty();
    }

    @Override
    public Optional<BasicPlayer> visit(Treasure treasure) {
        return Optional.empty();
    }
}
