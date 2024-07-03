package io.tomneh.canvengine.util.math;

import java.io.Serializable;

public class Float2D implements Serializable {
    /** An x coordinate of the math vector. */
    public float x;
    /** A y coordinate of the math vector. */
    public float y;

    // Constructors
    /** Initializes all instance fields by setting their values. */
    public Float2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
    /** Initializes by setting all instance fields to zero. */
    public static Float2D zero(){
        return new Float2D(0,0);
    }

    // Binary function.

    /** @return vector sum. */
    public Float2D add(Float2D other){
        return new Float2D(this.x +other.x, this.y +other.y);
    }

    /** @return vector difference. */
    public Float2D sub(Float2D other){
        return new Float2D(this.x -other.x, this.y -other.y);
    }

    /** @return dot product of two vectors. */
    public float dot(Float2D other){
        return this.x*other.x + this.y*other.y;
    }

    // Unary function.
    /** @return vector norm. */
    public double norm(Float2D other){
        return Math.sqrt((double) this.dot(other));
    }
}
