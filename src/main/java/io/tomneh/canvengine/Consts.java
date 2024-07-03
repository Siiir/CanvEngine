package io.tomneh.canvengine;

/**
 * Holds constants related to the game's settings and configurations.
 */
public class Consts {

    /**
     * The path to the game directory.
     */
    public static final String GAME_DIR = "./";

    /**
     * Use to convert the physical distance between objects to their on-screen distance.
     *
     * Example usage:
     * <code>int screenDist = (int) (physicalDist >> TO_SCREEN_METRIC_SH_R);</code>
     */
    public static final long TO_SCREEN_METRIC_SH_R = 32;

    /**
     * Use to convert the on-screen distance between objects to their physical distance.
     *
     * Example usage:
     * <code>long physicalDist = (long)onScreenDist << TO_PHYSIC_METRIC_SH_L;</code>
     *
     * Note that this constant has the same value as {@link #TO_SCREEN_METRIC_SH_R} and
     * can be used interchangeably.
     */
    public static final long TO_PHYSIC_METRIC_SH_L = TO_SCREEN_METRIC_SH_R;

    /** Ergonomic operand for unit conversions by multiplication.
     * <br><br>
     * Multiply number of <b>mili seconds</b> by {@linkplain #MS_TO_NS_MULTIPLIER} to get number of <b>nanoseconds</b>.
     * */
    public static final long MS_TO_NS_MULTIPLIER = 1_000_000;

    /** Ergonomic operand for unit conversions by multiplication.
     * <br><br>
     * Multiply number of <b>seconds</b> by {@linkplain #SEC_TO_NS_MULTIPLIER} to get number of <b>nanoseconds</b>.
     * */
    public static final long SEC_TO_NS_MULTIPLIER = 1_000 * MS_TO_NS_MULTIPLIER;

    /**
     * Minimum time between frames.
     * <br><br>
     * Game engine will use this value to minimize amount of frame updates.
     * */
    public static final int MIN_MS_BETWEEN_FRAMES = 16;

    /** Multiplication of {@link #MIN_MS_BETWEEN_FRAMES} */
    public static final long MIN_NS_BETWEEN_FRAMES= MS_TO_NS_MULTIPLIER * (long) MIN_MS_BETWEEN_FRAMES;
}
