package io.tomneh.canvengine.ui.awt.compos;

import io.tomneh.canvengine.components.render.RenderLayer;
import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;

public interface Drawable {
    DrawCommand drawCommand();
    RenderLayer getLayer();
}
