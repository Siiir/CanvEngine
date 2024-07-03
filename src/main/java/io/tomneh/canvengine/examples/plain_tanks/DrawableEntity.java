package io.tomneh.canvengine.examples.plain_tanks;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface DrawableEntity {
    void draw(Graphics graphics, ImageObserver observer);
}
