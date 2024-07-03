package io.tomneh.canvengine.ui.awt.cmd;

import java.awt.*;
import java.awt.image.ImageObserver;

public record DrawInstruction(Image image, int x, int y) implements DrawCommand{
    @Override
    public void draw(Graphics graphics, ImageObserver observer){
        graphics.drawImage(image, x, y, observer);
    }
    @Override
    public String toString(){
        return String.format("%s:  Draw `%s` at (%d,%d).",
                this.getClass(), this.image.getClass(), this.x, this.y);
    }
}
