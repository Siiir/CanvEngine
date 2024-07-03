package io.tomneh.canvengine.ui.awt.cmd;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface DrawCommand {
    void draw(Graphics graphics, ImageObserver observer);
}
