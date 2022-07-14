package treasuregame.factory.component;

import treasuregame.component.GameComponent;

import java.util.Optional;

/**
 * Interface used to create GameComponents through a specific T type
 * @param <T> the type of the specific data
 */
@FunctionalInterface
public interface GameComponentFactory<T> {
    Optional<GameComponent> fromSource(T source);
}
