package io.tomneh.canvengine.entity.samples;

import io.tomneh.canvengine.res.assets.ImageAsset;
import io.tomneh.canvengine.res.ImgProperties;
import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;
import io.tomneh.canvengine.ui.awt.cmd.DrawInstruction;
import io.tomneh.canvengine.ui.awt.compos.Drawable;
import io.tomneh.canvengine.components.render.RenderLayer;
import io.tomneh.canvengine.entity.Entity;

public class UncoloredCastle extends Entity implements Drawable {
    // Shared consts
    static final ImageAsset image= new ImageAsset(new ImgProperties(
            "backgrounds/uncolored_castle", 1535, 1024, 0
    ));

    @Override
    public DrawCommand drawCommand() {
        return new DrawInstruction(this.image.getRefToCachedImg(), 0, 0);
    }

    @Override
    public RenderLayer getLayer() {
        return RenderLayer.BACKGROUND;
    }
}
