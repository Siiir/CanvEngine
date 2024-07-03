package io.tomneh.canvengine.ui.awt.cmd;

import java.awt.*;
import java.awt.image.ImageObserver;

public record DrawNothing() implements DrawCommand {
    @Override
    public void draw(Graphics graphics, ImageObserver observer) {
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }
}
