package io.tomneh.canvengine.examples.plain_tanks;

public interface DynCircleCollider {
    int x(); int y();
    int radius();
    default boolean isCollidingWithCircle(int x, int y, int radius){
        double centerToCenterDist= Math.hypot(
                Math.subtractExact(this.x(),x),
                Math.subtractExact(this.y(),y)
        );

        return centerToCenterDist < Math.addExact( this.radius(), radius);
    }
    void receiveDamage(int damage, Scene scene);
}
