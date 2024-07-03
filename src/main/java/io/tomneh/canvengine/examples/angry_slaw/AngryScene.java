package io.tomneh.canvengine.examples.angry_slaw;

import io.tomneh.canvengine.res.assets.SoundAsset;
import io.tomneh.canvengine.Consts;
import io.tomneh.canvengine.entity.samples.SlawcioBall;
import io.tomneh.canvengine.entity.samples.UncoloredCastle;
import io.tomneh.canvengine.exec.ParallelExecutor;
import io.tomneh.canvengine.util.math.Float2D;
import io.tomneh.canvengine.util.math.Long2D;
import io.tomneh.canvengine.ui.awt.compos.Scene;
import javazoom.jl.decoder.JavaLayerException;

public class AngryScene extends Scene {
    public AngryScene(){
        super();
        new SoundAsset("music/Purple by Roa").getRefToCachedSound().playInLoopUsingExecutor();
        this.registerEntity(new UncoloredCastle()); // Background
        // Dynamic entities
        {
            final long shL= Consts.TO_PHYSIC_METRIC_SH_L;
            this.registerEntity( new SlawcioBall(
                    new Long2D(300l<<shL,300l<<shL), 50l<<shL,
                    new Float2D(10l<<32, 5l<<32)
            ) );
        }
    }
}
