package treasuregame.visitor.component;

import treasuregame.component.Mountain;
import treasuregame.component.playable.BasicPlayer;
import treasuregame.component.Treasure;

/**
 * Visitor used to print GameComponents
 */
public class ComponentToStringVisitor implements GameComponentVisitor<String>{
    @Override
    public String visit(BasicPlayer basicPlayer) {
        return "A - " + basicPlayer.getName() +
                " - " + basicPlayer.getPosition().x() +
                " - " + basicPlayer.getPosition().y() +
                " - " + basicPlayer.getOrientation() +
                " - " + basicPlayer.getFoundTreasures();
    }

    @Override
    public String visit(Mountain mountain) {
        return "M - " + mountain.getPosition().x() + " - " + mountain.getPosition().y();
    }

    @Override
    public String visit(Treasure treasure) {
        if(treasure.getRemainingTreasure() > 0){
            return "T - " + treasure.getPosition().x() + " - " + treasure.getPosition().y() + " - " + treasure.getRemainingTreasure();
        } else return "";
    }
}
