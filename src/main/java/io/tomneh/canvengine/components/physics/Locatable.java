package io.tomneh.canvengine.components.physics;

import io.tomneh.canvengine.Consts;
import io.tomneh.canvengine.components.Compo;
import io.tomneh.canvengine.util.math.Long2D;

public interface Locatable extends Compo {
    /** Uncategorized getter. */
    Long2D physicCords();

    /** Calc-getter. */
    public default Long2D onScreenCords(){
        var physicCords= this.physicCords();
        return new Long2D(
                physicCords.x >> Consts.TO_SCREEN_METRIC_SH_R,
                physicCords.y >> Consts.TO_SCREEN_METRIC_SH_R
        );
    }
}
