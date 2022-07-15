package treasuregame.game;

import treasuregame.action.IAction;
import treasuregame.component.GameComponent;
import treasuregame.component.IGame;
import treasuregame.component.Map;
import treasuregame.component.playable.Playable;
import treasuregame.visitor.action.DefaultMovementActionVisitor;
import treasuregame.visitor.component.PositionVisitor;
import treasuregame.observer.BasicGameObserver;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Class which represents a classic game described in the specs
 * V1.3
 */
public class BasicGameImpl implements IGame {

    private final int horizontal;
    private final int vertical;
    public BasicGameImpl(int horizontal, int vertical){
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    private final List<BasicGameObserver> observers = new ArrayList<>();
    private final List<GameComponent> components = new ArrayList<>();
    private final Queue<Playable> basicPlayers = new ArrayDeque<>();

    /**
     * GameComponents un/registration
     */
    @Override
    public void registerGameComponent(GameComponent component) {
        requireNonNull(component);
        components.add(component);
    }

    @Override
    public void unregisterGameComponent(GameComponent component) {
        requireNonNull(component);
        if (!components.remove(component)) throw new IllegalArgumentException();
    }

    /**
     * Main function of the basic game described in the specs
     * V1.3
     */
    @Override
    public void runGame() {
        // var init
        var currentMap = new Map(horizontal, vertical, components);
        var positionVisitor = new PositionVisitor();
        var movementVisitor = new DefaultMovementActionVisitor(currentMap);

        notifyStart();

        // updates each component at its position
        components.forEach(positionVisitor::visit);

        // retrieves players among registered components
        components.stream()
                .filter(Playable.class::isInstance)
                .map(Playable.class::cast)
                .forEach(basicPlayers::add);

        // starting turn game
        boolean isDone = basicPlayers.isEmpty();
        while(!isDone){
            // poll the next player
            var currentPlayer = basicPlayers.poll();

            currentPlayer.acquireNextAction()
                    // if player has an action to perform
                    .ifPresent(action -> {
                        // perform action
                        movementVisitor.applyMovement(action, currentPlayer);
                        // add the player to the end of the queue for its next action
                        basicPlayers.add(currentPlayer);
                        notifyAction(currentPlayer, action);
                    });

            // check if there is still a waiting player
            isDone = basicPlayers.isEmpty();
        }

        notifyStop();
    }

    /**
     * Observers notifications
     */
    private void notifyStart(){
        observers.forEach(observer -> observer.onGameStart(this) );
    }
    private void notifyAction(Playable playable, IAction action){
        observers.forEach(observer -> observer.onGameChange(this, playable, action));
    }
    private void notifyStop(){
        observers.forEach(observer -> observer.onGameStop(this) );
    }


    /**
     * Observers un/registration
     */
    public void registerObserver(BasicGameObserver observer){
        observers.add(requireNonNull(observer));
    }

    public void unregisterObserver(BasicGameObserver observer){
        if (!observers.remove(requireNonNull(observer))) throw new IllegalArgumentException();
    }

    public List<? extends GameComponent> getComponents(){
        return Collections.unmodifiableList(components);
    }

    /**
     * Used to print a map's characteristics
     * V0.5
     */
    public String getSizeMap() {
        var horizontalValue = horizontal + 1;
        var verticalValue = vertical + 1;
        return "C - " + horizontalValue + " - " + verticalValue;
    }
}
