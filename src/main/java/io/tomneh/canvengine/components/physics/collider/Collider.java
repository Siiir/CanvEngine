package io.tomneh.canvengine.components.physics.collider;

import io.tomneh.canvengine.components.Compo;

public interface Collider extends Compo {
    public boolean collidesWith(BallCollider circle);
    public default boolean collidesWith(Collider other){
        if (this instanceof BallCollider && other instanceof BallCollider){
            var lhs= (BallCollider)this; var rhs= (BallCollider)other;
            return this.collidesWith(rhs);
        }else {
            throw new UnsupportedOperationException(
                    "All cases should be implemented. If you see this exception contact app developers."
            );
        }
    }
}

