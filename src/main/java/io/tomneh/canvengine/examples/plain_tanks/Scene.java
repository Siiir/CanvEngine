package io.tomneh.canvengine.examples.plain_tanks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scene extends JPanel implements Serializable, DynEntity {
    static StaticSceneObj[] backgroundEntities = new StaticSceneObj[]{
        // background elements
        new StaticSceneObj(Res.getTexture("Grassland"), 0, 0, true),
        new StaticSceneObj(Util.resized(Res.getTexture("VioletFlowers"), 100, 100), 100, 100),
        new StaticSceneObj(Util.resized(Res.getTexture("VioletFlowers"), 120, 120), 300, 150),
        new StaticSceneObj(Util.resized(Res.getTexture("Rose"), 200, 150),  520, 820),
        new StaticSceneObj(Util.resized(Res.getTexture("Bush"), 100, 60), 800, 600)
    };
    transient ArrayList<DynSceneObj> dynSceneObjsToAdd= new ArrayList<>();
    transient ArrayList<Object> gameObjToRem= new ArrayList<>();

    State state= new State();

    public Scene(){
        super();
        this.dynSceneObjsToAdd.add( new Tank(
            200, 800, "Winnie",
            new Tank.Hull(
                    Res.getTexture("Winnie"), 80, Math.PI/4,
                    new Tank.Hull.Engine(1l<<29, 0b11l<<26, 0.0007)
            ),
            new Tank.Turret(
                    Res.getTexture("Pencil"), 100, 0,
                    new Tank.Turret.Engine(0.001),
                    new Tank.Turret.ProjectileTraits(
                            Res.getTexture("CrumpledPaperBall"), 10,
                            1l<<31, 2
                    ),
                    2000, "PaperSound", new Tank.Turret.Keybindings(
                            KeyEvent.VK_G, KeyEvent.VK_J,
                            KeyEvent.VK_H
                    )
            ),
            new Tank.Keybindings(
                    KeyEvent.VK_W, KeyEvent.VK_S,
                    KeyEvent.VK_A, KeyEvent.VK_D
            ),
            10, "SteveOOFSound"
        ) );
        this.dynSceneObjsToAdd.add( new Tank(
                1800, 200, "Piglet",
                new Tank.Hull(
                        Res.getTexture("Piglet"), 70, -Math.PI/2,
                        new Tank.Hull.Engine(1l<<30, 0b11l<<28, 0.0014)
                ),
                new Tank.Turret(
                        Res.getTexture("MediumTurret"), 90, -Math.PI*0.75,
                        new Tank.Turret.Engine(0.001),
                        new Tank.Turret.ProjectileTraits(
                                Res.getTexture("Potato"), 20,
                                1l<<32, 4
                        ),
                        6500, "TankFire", new Tank.Turret.Keybindings(
                                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                                KeyEvent.VK_UP
                        )
                ),
                new Tank.Keybindings(
                        KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5,
                        KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6
                ),
                4, "OughSoundEffect"
        ) );
    }

    class State implements Serializable{
        final public int millisBetweenFrames= 18; // 60 FPS
        // Transients
        transient public AtomicBoolean isPaused= new AtomicBoolean(false);
        transient HashSet<DynCircleCollider> colliders= new HashSet<>();
        transient HashSet<DynSceneObj> dynEntities= new HashSet<>();

        private void writeObject(java.io.ObjectOutputStream out)
                throws IOException {
            out.defaultWriteObject();
            out.writeBoolean(this.isPaused.get());
            // colliders ignored
            { // this.dynEntities
                out.writeInt(this.dynEntities.size());
                for (var dynEntity :
                        this.dynEntities) {
                    out.writeObject(dynEntity);
                }
            }
        }
        private void readObject(java.io.ObjectInputStream in)
                throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            { // Transient
                this.isPaused = new AtomicBoolean(in.readBoolean());
                this.colliders= new HashSet<>();
                { // this.dynEntities
                    int size = in.readInt();
                    this.dynEntities = new HashSet<>(size);
                    for (; size > 0; size--) {
                        DynSceneObj next = (DynSceneObj) in.readObject();
                        this.dynEntities.add(next);
                        if(next instanceof DynCircleCollider){
                            this.colliders.add((DynCircleCollider) next);
                        }

                    }
                }
            }
        }
    }
    @Override
    public void updateState(MainWindow eventSource) {
        { // Updating state of registered `DynEntity`-ies.
            for (var dynEntity :
                    this.state.dynEntities) {
                dynEntity.updateState(eventSource);
            }
        }
        { // Clearing dead
            for (Object gameObjToRem :
                    this.gameObjToRem) {
                this.remDynGameObj(gameObjToRem);
            }
            this.gameObjToRem.clear();
        }
        { // Adding born
            for (DynSceneObj dynSceneObj :
                    this.dynSceneObjsToAdd) {
                this.state.dynEntities.add(dynSceneObj);
                if (dynSceneObj instanceof DynCircleCollider){
                    this.state.colliders.add((DynCircleCollider) dynSceneObj);
                }
            }
            this.dynSceneObjsToAdd.clear();
        }
    }

    private void remDynGameObj(Object gameObjToRem) {
        this.state.dynEntities.remove(gameObjToRem);
        this.state.colliders.remove(gameObjToRem);
    }

    public void updateFrame(){
        if(state.isPaused.get()){
            return;
        }
        this.updateState((MainWindow) this.getTopLevelAncestor());
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (StaticSceneObj backgroundEntity :
                backgroundEntities) {
            backgroundEntity.draw(g, this);
        }
        for (DynSceneObj dynEntity :
                this.state.dynEntities) {
            dynEntity.draw(g, this);
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(this.state);
    }
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException{
        this.state= (State) in.readObject();
        // Transient
        this.dynSceneObjsToAdd= new ArrayList<>();
        this.gameObjToRem= new ArrayList<>();
    }
}
