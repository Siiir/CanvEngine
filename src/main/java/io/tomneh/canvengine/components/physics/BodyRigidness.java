package io.tomneh.canvengine.components.physics;

import io.tomneh.canvengine.components.Compo;
import io.tomneh.canvengine.components.physics.collider.Collider;
import io.tomneh.canvengine.ui.awt.SceneManager;
import io.tomneh.canvengine.util.math.Float2D;

/** Serves as a proxy. Facilitates indirect control of entity's physical based behaviour. */
public interface BodyRigidness extends Locatable, Compo {
    /** @return a collider that represents this object's collision area.
     * <br><br>
     * This is uncategorized getter that can be implemented in many ways.*/
    Collider collider();
}