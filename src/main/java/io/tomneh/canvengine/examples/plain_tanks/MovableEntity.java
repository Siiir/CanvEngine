package io.tomneh.canvengine.examples.plain_tanks;

import java.io.Serializable;

public abstract class MovableEntity implements Serializable {
    long virtualX;
    long virtualY;

    public MovableEntity(long virtualX, long virtualY) {
        this.virtualX = virtualX;
        this.virtualY = virtualY;
    }

    // Getters
    int physicalX(){
        return (int) (this.virtualX>>32);
    }
    int physicalY(){
        return (int) (this.virtualY>>32);
    }

    // Normal methods
    public void vMoveFwd(double rotInRads, long vStreet){
        long vMoveX= (long) (vStreet * Math.sin(rotInRads));
        long vMoveY= (long) (vStreet * Math.cos(rotInRads));

        this.virtualX+= vMoveX;
        this.virtualY-= vMoveY;
    }
}
