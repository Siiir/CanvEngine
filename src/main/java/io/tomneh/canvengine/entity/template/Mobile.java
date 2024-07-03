package io.tomneh.canvengine.entity.template;

import io.tomneh.canvengine.components.physics.Movable;
import io.tomneh.canvengine.entity.Entity;
import io.tomneh.canvengine.util.math.Long2D;

public class Mobile extends Entity implements Movable {
    protected Long2D physicCords;

    public Mobile(Long2D initPhysicCords) {
        this.physicCords = initPhysicCords;
    }

    @Override
    public Long2D physicCords() {
        return physicCords;
    }
}
