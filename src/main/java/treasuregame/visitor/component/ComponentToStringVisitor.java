package treasuregame.visitor.component;

import treasuregame.component.Mountain;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.Treasure;

/**
 * Visitor used to print GameComponents
 */
public class ComponentToStringVisitor implements GameComponentVisitor<StringBuilder>{
    @Override
    public StringBuilder visit(BasicPlayer basicPlayer) {
        return new StringBuilder("A - " + basicPlayer.getName() +
                " - " + basicPlayer.getPosition().x() +
                " - " + basicPlayer.getPosition().y() +
                " - " + basicPlayer.getOrientation() +
                " - " + basicPlayer.getFoundTreasures());
    }

    @Override
    public StringBuilder visit(Mountain mountain) {
        return new StringBuilder("M - " + mountain.getPosition().x() + " - " + mountain.getPosition().y());
    }

    @Override
    public StringBuilder visit(Treasure treasure) {
        if(treasure.getRemainingTreasure() > 0){
            return new StringBuilder("T - " + treasure.getPosition().x() + " - " + treasure.getPosition().y() + " - " + treasure.getRemainingTreasure());
        } else return new StringBuilder();
    }
}
