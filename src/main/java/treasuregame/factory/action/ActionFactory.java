package treasuregame.factory.action;

import treasuregame.action.IAction;

import java.util.Optional;

/**
 * Class used to create IAction object from a specific T data type
 * @param <T> the type of the given data
 */
public interface ActionFactory<T> {
    Optional<? extends IAction> fromSource(T source);
}
