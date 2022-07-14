package treasuregame.component;

import treasuregame.component.GameComponent;
import treasuregame.observer.BasicGameObserver;

/**
 * Interface which represents a Game
 * Games needs to register / unregister component and run
 * They also can register Observers, implemented with default because it ain't always necessary
 */
public interface IGame {
    void registerGameComponent(GameComponent component);
    void unregisterGameComponent(GameComponent component);
    void runGame();

    default void registerObserver(BasicGameObserver observer){

    }

    default void unregisterObserver(BasicGameObserver observer){

    }
}
