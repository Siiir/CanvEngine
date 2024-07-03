package io.tomneh.canvengine.components.physics.collider;

import io.tomneh.canvengine.components.physics.Locatable;
import io.tomneh.canvengine.util.math.Long2D;

public interface BallCollider extends Locatable, Collider {
    long physicRadius();

    @Override
    public default boolean collidesWith(BallCollider rhs) {
        Long2D lhsCords = this.physicCords();
        Long2D rhsCords = rhs.physicCords();

        long distX = lhsCords.x - rhsCords.x;
        long distY = lhsCords.y - rhsCords.y;
        long distanceSquared = distX * distX + distY * distY;

        long radiusSum = this.physicRadius() + rhs.physicRadius();
        long radiusSumSquared = radiusSum * radiusSum;

        return distanceSquared <= radiusSumSquared;
    }
}
