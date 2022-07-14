package treasuregame.observer;

import treasuregame.action.IAction;
import treasuregame.component.playable.Playable;
import treasuregame.game.BasicGameImpl;

/**
 * Observer of a basic game
 * Three actions implemented: start, change, stop
 */
public interface BasicGameObserver {
    default void onGameStart(BasicGameImpl game) { }
    default void onGameChange(BasicGameImpl game, Playable playable, IAction action) { }
    default void onGameStop(BasicGameImpl game) { }
}
