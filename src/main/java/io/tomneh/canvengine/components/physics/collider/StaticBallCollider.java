package io.tomneh.canvengine.components.physics.collider;

import io.tomneh.canvengine.util.math.Long2D;

public class StaticBallCollider implements BallCollider {
    final Long2D physicCords;
    final long radius;

    public StaticBallCollider(Long2D physicCords, long radius) {
        this.physicCords = physicCords;
        this.radius = radius;
    }

    @Override
    public Long2D physicCords() {
        return this.physicCords;
    }
    @Override
    public long physicRadius() {
        return this.radius;
    }
}
