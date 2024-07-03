package io.tomneh.canvengine.components.physics;

import io.tomneh.canvengine.util.math.Long2D;

/** A special kind of {@link Locatable}
 *   that guarantees a reference access to physical coordinates of entity allowing to modify them. */
public interface Movable extends Locatable {

    /** A <b><u>reference</u> getter</b> to physical coordinates of the entity.
     * <br><br>
     * <h3>Warning</h3>
     * While you were allowed to implement
     *  {@link Locatable#physicCords()} as a <u>value</u> getter,
     *  this override of mentioned method must return a <u>reference</u>
     *  that can be used to set new coordinates for owner entity.
     *  */
    @Override
    Long2D physicCords();
}
