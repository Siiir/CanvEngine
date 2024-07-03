package io.tomneh.canvengine.entity;

import io.tomneh.canvengine.util.Scene;

import java.io.Serializable;

/**
 * Represents on-{@link Scene} entity.
 * <br><br>
 * Subclasses of {@linkplain Entity} are expected to implement subinterfaces of
 * {@link io.tomneh.canvengine.components.Compo} and/or override
 * {@link #registerComposOn(Scene)} & {@link #unregisterComposFrom(Scene)},
 * <b>though</b> are not required to do so.
 * */
public abstract class Entity implements Serializable {
    /** Override this if faster or more specialized registration is needed. */
    public void registerComposOn(Scene scene){
        scene.slowlyRegCompos(this);
    }
    /** Override this if faster or more specialized unregistration is needed. */
    public void unregisterComposFrom(Scene scene){
        scene.slowlyUnregCompos(this);
    }
}
