package treasuregame.factory.game;

import treasuregame.component.IGame;

import java.util.Optional;

/**
 * Functional interface used to create Games through a specific T type
 * @param <T> type of the given data
 */
@FunctionalInterface
public interface GameFactory<T> {
    Optional<IGame> fromSource(T source);
}
