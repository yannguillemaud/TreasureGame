package treasuregame.component.playable;

import treasuregame.component.GameComponent;

/**
 * Interface used in the first implementations in order to differentiate player among other gamecomponent
 */
public interface Playable extends GameComponent {
    void updateOrientation(Orientation orientation);

    Orientation getOrientation();
}
