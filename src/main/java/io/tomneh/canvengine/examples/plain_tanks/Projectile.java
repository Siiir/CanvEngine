package io.tomneh.canvengine.examples.plain_tanks;

import io.tomneh.canvengine.examples.plain_tanks.MovableEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.stream.Collectors;

public class Projectile extends MovableEntity implements DynSceneObj {
    transient BufferedImage skin;
    int radius;
    double rotInRads;

    long vSpeedPerMillis;
    int onContactDamage;

    public Projectile(
            long virtualX, long virtualY, // 0
            BufferedImage skin, int radius, double rotInRads, // 1
            long vSpeedPerMillis, int onContactDamage // 2
            ) {
        super(virtualX, virtualY); // 0
        { // 1
            this.skin = Util.resized(
                    Util.rotated( skin, rotInRads),
                    2*radius, 2*radius
            );
            this.radius= radius;
            this.rotInRads = rotInRads;
        }
        { // 2
            this.vSpeedPerMillis = vSpeedPerMillis;
            this.onContactDamage = onContactDamage;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        ImageIO.write(this.skin, "png", out);
    }
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        this.skin= ImageIO.read(in);
    }

    @Override
    public void draw(Graphics graphics, ImageObserver observer) {
        graphics.drawImage(this.skin, this.physicalX()-this.radius, this.physicalY()-this.radius, observer);
    }

    @Override
    public void updateState(MainWindow eventSource) {
        Scene scene = eventSource.getScene();
        { // Moving
            long vMove= scene.state.millisBetweenFrames * this.vSpeedPerMillis;
            this.vMoveFwd(this.rotInRads, vMove);
        }
        // Trying to destroy self and harm encountered collider.
        for (DynCircleCollider collider :
                scene.state.colliders) {
            if(collider.isCollidingWithCircle(this.physicalX(), this.physicalY(), this.radius)){
                System.out.println("Projectile bumped into `" + collider +"`.");
                scene.gameObjToRem.add(this);
                collider.receiveDamage(this.onContactDamage, scene);
                break;
            }
        }
        { // Destroy self if it has crossed the game border
            int maxX, maxY;
            {
                Dimension sceneDimension = scene.getSize();
                maxX= sceneDimension.width;
                maxY= sceneDimension.height;
            }
            int x= this.physicalX(), y= this.physicalY();
            if(x<0 || maxX<x || y<0 || maxY<y){
                scene.gameObjToRem.add(this);
            }
        }
    }
}
