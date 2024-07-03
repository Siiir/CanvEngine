package io.tomneh.canvengine.examples.plain_tanks;

import io.tomneh.canvengine.res.assets.SoundAsset;
import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;

class TankPart implements Serializable {
    // Computed
    transient BufferedImage recentTexture = null;
    transient double recentRotInRads = 0. / 0.;
    // Args
    transient BufferedImage baseTexture;
    int radius;
    double rotInRads;

    public TankPart(BufferedImage skin, int radius, double rotInRads) {
        this.baseTexture = Util.resized(skin, 4 * radius, 4 * radius);
        this.radius = radius;
        this.rotInRads = rotInRads;
    }

    public void rotateRight(double angleInRads) {
        this.rotInRads += angleInRads;
    }

    public void draw(Tank owner, Graphics graphics, ImageObserver observer) {
        BufferedImage texture = this.recentRotInRads == this.rotInRads ?
                this.recentTexture
                : Util.resized(
                Util.rotated(this.baseTexture, this.rotInRads),
                2 * this.radius, 2 * this.radius
        );
        graphics.drawImage(texture, owner.x() - this.radius, owner.y() - this.radius, observer);
        {
            this.recentRotInRads = this.rotInRads;
            this.recentTexture = texture;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        ImageIO.write(this.baseTexture, "png", out);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        // Args
        in.defaultReadObject();
        this.baseTexture = ImageIO.read(in);
        // Computed
        this.recentTexture = null;
        this.recentRotInRads = 0. / 0.;
    }
}

public class Tank extends MovableEntity implements DynSceneObj, DynCircleCollider {
    private final String name;
    final Hull hull;
    final Turret turret;
    final Keybindings keybindings;
    final int baseHealth;
    int health;
    final SoundAsset soundOfHarm;

    public Tank(
            int x, int y, String name, // 0
            Hull hull, Turret turret, Keybindings keybindings, // 1
            int baseHealth, String soundOfHarmName // 2
    ) {
        super((long) x << 32, (long) y << 32);
        this.name = name; // 0
        { // 1
            this.hull = hull;
            this.turret = turret;
            this.keybindings = keybindings;
        }
        { // 2
            this.health = this.baseHealth = baseHealth;
            this.soundOfHarm = new SoundAsset(soundOfHarmName);
        }
    }

    @Override
    public int x() {
        return this.physicalX();
    }

    @Override
    public int y() {
        return this.physicalY();
    }

    @Override
    public int radius() {
        return this.hull.radius;
    }

    static class Hull extends TankPart {
        final Engine engine;

        public Hull(BufferedImage skin, int radius, double rotInRads,
                    Engine engine) {
            super(skin, radius, rotInRads);
            this.engine = engine;
        }

        static class Engine implements Serializable {
            final long vFwdSpeedPerMillis, vBwdSpeedPerMillis;
            final double rotSpeedPerMillis;

            public Engine(long vFwdSpeedPerMillis, long vBwdSpeedPerMillis, double rotSpeedPerMillis) {
                this.vFwdSpeedPerMillis = vFwdSpeedPerMillis;
                this.vBwdSpeedPerMillis = vBwdSpeedPerMillis;
                this.rotSpeedPerMillis = rotSpeedPerMillis;
            }
        }
    }

    static class Turret extends TankPart {
        final Engine engine;
        final ProjectileTraits projectileTraits;
        final int reloadTimeInMillis;
        int millisToWaitBeforeShot;
        final SoundAsset shotSound;
        final Keybindings keybindings;

        public Turret(BufferedImage skin, int radius, double rotInRads, // 0
                      Engine engine, ProjectileTraits projectileTraits, // 1
                      int reloadTimeInMillis, String shotSoundName, // 2
                      Keybindings keybindings  // 3
        ) {
            super(skin, radius, rotInRads); // 0
            this.engine = engine;
            this.projectileTraits = projectileTraits; // 1
            { // 2
                this.reloadTimeInMillis = reloadTimeInMillis;
                this.millisToWaitBeforeShot = 0;
                this.shotSound = new SoundAsset( shotSoundName );
            }
            this.keybindings = keybindings; // 3
        }

        public void updateState(MainWindow eventSource, Tank owner) {
            var pressedKeys = eventSource.pressedKeys;
            int millisBetweenFrames = eventSource.getScene().state.millisBetweenFrames;
            { // Rotate
                double rotSpeedPerFrame = this.engine.rotSpeedPerMillis * millisBetweenFrames;
                // Left
                if (pressedKeys.contains(this.keybindings.rotLeft)) {
                    this.rotateRight(-rotSpeedPerFrame);
                }
                // Right
                if (pressedKeys.contains(this.keybindings.rotRight)) {
                    this.rotateRight(rotSpeedPerFrame);
                }
            }
            { // Shooting
                // Reload
                if (this.millisToWaitBeforeShot > 0) {
                    this.millisToWaitBeforeShot -= millisBetweenFrames;
                }
                // Shoot
                if (pressedKeys.contains(this.keybindings.shoot)) {
                    this.tryShootWithReload(eventSource.getScene(), owner);
                }
            }
        }

        public void tryShootWithReload(Scene scene, Tank shooter) {
            if (this.millisToWaitBeforeShot <= 0) {
                this.shootForFree(scene, shooter);
                this.millisToWaitBeforeShot = this.reloadTimeInMillis;
            }
        }

        public void shootForFree(Scene scene, Tank shooter) {
            this.shotSound.getRefToCachedSound().playUsingExecutor();
            // Construction of projectile
            Projectile projectileToShoot = new Projectile(
                    shooter.virtualX, shooter.virtualY,
                    this.projectileTraits.skin, this.projectileTraits.radius, this.rotInRads,
                    this.projectileTraits.vSpeedPerMillis, this.projectileTraits.onContactDemage
            );
            {// Correcting coordinates
                // The minimal, virtual distance that must be traversed, so that the projectile doesn't explode on the shooter
                long minVStreetToTraverse = (long) (shooter.hull.radius + this.radius) << 32;
                projectileToShoot.vMoveFwd(projectileToShoot.rotInRads, minVStreetToTraverse);
            }
            // Registering correctly spawned projectile.
            scene.dynSceneObjsToAdd.add(projectileToShoot);
        }

        static class Engine implements Serializable {
            final double rotSpeedPerMillis;

            public Engine(double rotSpeedPerMillis) {
                this.rotSpeedPerMillis = rotSpeedPerMillis;
            }
        }

        static class ProjectileTraits implements Serializable {
            transient BufferedImage skin;
            int radius;
            long vSpeedPerMillis;
            int onContactDemage;

            public ProjectileTraits(BufferedImage skin, int radius,
                                    long vSpeedPerMillis, int onContactDemage) {
                {
                    this.skin = skin;
                    this.radius = radius;
                }
                {
                    this.vSpeedPerMillis = vSpeedPerMillis;
                    this.onContactDemage = onContactDemage;
                }
            }

            private void writeObject(java.io.ObjectOutputStream out) throws IOException {
                out.defaultWriteObject();
                ImageIO.write(this.skin, "png", out);
            }

            private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
                in.defaultReadObject();
                this.skin = ImageIO.read(in);
            }
        }

        static class Keybindings implements Serializable {
            final int rotLeft, rotRight;
            final int shoot;

            public Keybindings(int rotLeft, int rotRight, int shoot) {
                this.rotLeft = rotLeft;
                this.rotRight = rotRight;
                this.shoot = shoot;
            }
        }
    }

    static class Keybindings implements Serializable {
        final int moveFwd, moveBwd;
        final int rotLeft, rotRight;

        public Keybindings(int moveFwd, int moveBwd,
                           int rotLeft, int rotRight) {
            this.moveFwd = moveFwd;
            this.moveBwd = moveBwd;
            this.rotLeft = rotLeft;
            this.rotRight = rotRight;
        }
    }

    public void receiveDamage(int damage, Scene rootScene) {
        this.health = Math.subtractExact(this.health, damage);
        this.soundOfHarm.getRefToCachedSound().playUsingExecutor();
        if (this.health <= 0) {
            rootScene.gameObjToRem.add(this);
            System.out.println(
                    this + (this.health == 0 ?
                            " killed."
                            : " overkilled with " + (-this.health) + " damage."
                    )
            );
        }
    }

    @Override
    public void draw(Graphics graphics, ImageObserver observer) {
        hull.draw(this, graphics, observer);
        turret.draw(this, graphics, observer);
    }

    @Override
    public String toString() {
        return "Tank{" +
                "name='" + name + '\'' +
                '}';
    }

    public void vMoveFwd(long vStreet) {
        this.vMoveFwd(this.hull.rotInRads, vStreet);
    }

    private void forceInsideScene(Scene scene) {
        long minVCord = (long) this.radius() << 32;
        long maxVX, maxVY;
        {
            Dimension sceneDimension = scene.getSize();
            maxVX = (long) (sceneDimension.width - this.radius()) << 32;
            maxVY = (long) (sceneDimension.height - this.radius()) << 32;
        }
        this.virtualX = Math.max(minVCord, Math.min(this.virtualX, maxVX));
        this.virtualY = Math.max(minVCord, Math.min(this.virtualY, maxVY));
    }

    public void rotateRight(double angleInRads) {
        this.hull.rotateRight(angleInRads);
        this.turret.rotateRight(angleInRads);
    }

    @Override
    public void updateState(MainWindow eventSource) {
        var pressedKeys = eventSource.pressedKeys;
        var millisBetweenFrames = eventSource.getScene().state.millisBetweenFrames;
        { // Tank moves
            { // Changes location
                // Fwd
                if (pressedKeys.contains(this.keybindings.moveFwd)) {
                    long vFwdSpeedPerFrame = this.hull.engine.vFwdSpeedPerMillis * millisBetweenFrames;
                    this.vMoveFwd(vFwdSpeedPerFrame);
                }
                // Bwd
                if (pressedKeys.contains(this.keybindings.moveBwd)) {
                    long vBwdSpeedPerFrame = this.hull.engine.vBwdSpeedPerMillis * millisBetweenFrames;
                    this.vMoveFwd(-vBwdSpeedPerFrame);
                }
            }
            { // Rotates
                double vRotSpeedPerFrame = this.hull.engine.rotSpeedPerMillis * millisBetweenFrames;
                // Left
                if (pressedKeys.contains(this.keybindings.rotLeft)) {
                    this.rotateRight(-vRotSpeedPerFrame);
                }
                // Right
                if (pressedKeys.contains(this.keybindings.rotRight)) {
                    this.rotateRight(vRotSpeedPerFrame);
                }
            }
            // Is corrected if necessary
            this.forceInsideScene(eventSource.getScene());
        }
        turret.updateState(eventSource, this);
    }
}
