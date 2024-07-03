package io.tomneh.canvengine.util.math;

import java.io.Serializable;

/** Structure that facilitates operations on mathematical 2D vectors. */
public class Long2D implements Serializable {

    /** The `x` coordinate. */
    public long x;
    /** The `y` coordinate. */
    public long y;

    // Constructors
    /** Initializes all instance fields by setting their values. */
    public Long2D(long x, long y) {
        this.x = x;
        this.y = y;
    }
    /** Initializes by setting all instance fields to zero. */
    public static Long2D zero(){
        return new Long2D(0,0);
    }

    /** @return vector sum. */
    public Long2D add(Long2D other) {
        return new Long2D(this.x + other.x, this.y + other.y);
    }

    /** @return vector difference. */
    public Long2D sub(Long2D other) {
        return new Long2D(this.x - other.x, this.y - other.y);
    }

    /** @return a dot product. */
    public long dot(Long2D other) {
        return this.x * other.x + this.y * other.y;
    }

    /** @return a math vector norm. */
    public double norm() {
        return Math.sqrt(this.dot(this));
    }
}