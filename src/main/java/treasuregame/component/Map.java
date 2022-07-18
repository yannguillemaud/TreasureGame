package treasuregame.component;

import treasuregame.component.playable.BasicPlayer;
import treasuregame.visitor.component.GameComponentVisitor;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Class used to represent a map and its gamecomponents
 * Uses a manager for logic delegation (Single responsability)
 */
public class Map {
    private static class MapManager {
        private static final GameComponentVisitor<Boolean> isBlockingVisitor = new GameComponentVisitor<>() {
            @Override
            public Boolean visit(BasicPlayer playable) {
                return true;
            }

            @Override
            public Boolean visit(Mountain mountain) {
                return true;
            }

            @Override
            public Boolean visit(Treasure treasure) {
                return false;
            }
        };

        private final int mapX;
        private final int mapY;
        private java.util.Map<Position, List<GameComponent>> currentMap;

        private MapManager(int mapX, int mapY, List<GameComponent> components){
            this.mapX = mapX;
            this.mapY = mapY;
            this.currentMap = components.stream()
                    .collect(Collectors.groupingBy(GameComponent::getPosition));
            if(currentMap.values().stream()
                    .flatMap(Collection::stream)
                    .anyMatch(component -> !isInitAuthorized(component.getPosition())))
            {
                throw new IllegalArgumentException();
            }
        }

        private boolean isAvailable(GameComponent component, Position position){
            return isInMap(position) && !isBlocked(component, position);
        }

        /**
         * Determines if a position is between the defined ranges
         * @param position
         * @return true if position is in range, false otherwise
         */
        private boolean isInMap(Position position){
            return position.x() >= 0 && position.x() <= mapX
                    && position.y() >= 0 && position.y() <= mapY;
        }

        /**
         * Determines if a position if blocked by components that are currently on the position
         * @param position
         * @return true if a component blocks the position, false otherwise
         */
        private boolean isBlocked(GameComponent component, Position position){
            return componentsOnCase(position).stream()
                    .filter(not(existing -> existing.equals(component)))
                    .anyMatch(isBlockingVisitor::visit);
        }

        private boolean isInitAuthorized(Position position){
            return isInMap(position) && componentsOnCase(position).stream()
                    .map(isBlockingVisitor::visit)
                    .filter(Boolean::booleanValue)
                    .count() < 2;
        }

        /**
         * Returns every game components of a specific position
         * @param position
         * @return a List of GameComponents
         */
        private List<GameComponent> componentsOnCase(Position position){
            return Collections.unmodifiableList(currentMap.getOrDefault(position, new ArrayList<>()));
        }
    }

    private final List<GameComponent> components;
    private final MapManager mapManager;

    public Map(int horizontal, int vertical, List<GameComponent> componentList) {
        if(horizontal < 0 || vertical < 0) throw new IllegalArgumentException();
        this.components = Objects.requireNonNull(componentList);
        this.mapManager = new MapManager(horizontal, vertical, components);
    }

    /**
     * Determines if a position is available for a component
     * @param position
     * @return
     */
    public boolean isAvailable(GameComponent component, Position position){
        return mapManager.isAvailable(component, position);
    }

    /**
     * Applies a GameComponent visitor to each component on a specific position
     * Example: collision simulating
     * @param position the position where the visitor will be applied
     * @param visitor the visitor to apply to each component
     */
    public void applyOnComponentsOnCase(Position position, GameComponentVisitor<?> visitor){
        Objects.requireNonNull(position);
        mapManager.componentsOnCase(position).forEach(visitor::visit);
    }
}
