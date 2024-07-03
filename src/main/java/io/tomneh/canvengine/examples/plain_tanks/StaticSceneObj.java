package io.tomneh.canvengine.examples.plain_tanks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class StaticSceneObj implements DrawableEntity {
    BufferedImage image;
    int x, y;
    int leftTopCornerX, leftTopCornerY;

    public StaticSceneObj(BufferedImage image, int x, int y) {
        this(image, x, y, false);
    }
    public StaticSceneObj(BufferedImage image, int x, int y, boolean areLeftTopCornerCords) {
        this.image = image;
        if(areLeftTopCornerCords){
            this.leftTopCornerX= x;
            this.leftTopCornerY= y;
            this.x= x+this.image.getWidth()/2;
            this.y= y+this.image.getHeight()/2;
        }else {
            this.x = x;
            this.y = y;
            this.leftTopCornerX= this.x-this.image.getWidth()/2;
            this.leftTopCornerY= this.y-this.image.getHeight()/2;
        }
    }

    @Override
    public void draw(Graphics graphics, ImageObserver observer) {
        graphics.drawImage(this.image,
                this.leftTopCornerX, this.leftTopCornerY,
                observer);
    }
}
