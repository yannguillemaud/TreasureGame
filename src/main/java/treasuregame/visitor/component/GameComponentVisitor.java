package treasuregame.visitor.component;

import treasuregame.component.*;
import treasuregame.component.playable.BasicPlayer;

import java.util.Objects;

/**
 * Interface used as a GameComponent visitor
 * Can retrieve data with <T> or used as function distribution (<Void>)
 * @param <T> the data type of the returned information about a specific GameComponent
 */
public interface GameComponentVisitor<T> {
       T visit(BasicPlayer playable) ;
       T visit(Mountain mountain) ;
       T visit(Treasure treasure) ;
       default T visit(GameComponent component){
              Objects.requireNonNull(component);
              return switch(component){
                     case BasicPlayer basicPlayer -> visit(basicPlayer);
                     case Treasure treasure -> visit(treasure);
                     case Mountain mountain -> visit(mountain);
                     default -> throw new RuntimeException();
              };
       }
}