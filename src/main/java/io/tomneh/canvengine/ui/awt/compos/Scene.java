package io.tomneh.canvengine.ui.awt.compos;

import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;
import io.tomneh.canvengine.components.render.RenderLayer;
import io.tomneh.canvengine.entity.Entity;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Game logic happens here.
 * <br><br>
 * {@linkplain Scene} doesn't do image rendering, but can produce `DrawCommand`,
 * when prompted.
 * {@link DrawCommand} can be passed to graphic renderer operating on arbitrary thread.
 */
public class Scene extends io.tomneh.canvengine.util.Scene implements Drawable {
    // Regularly processed components

    /** Stores all registered drawable components organized into {@link RenderLayer}s.
     * <br><br>
     * They can be used while invoking <code>this.{@link #drawCommand()}</code>
     *  to create a command for scene renderer.*/
    transient protected TreeMap<RenderLayer,HashSet<Drawable>> drawables;

    // Init methods

    /** Initializes all transient fields of <code>this</code>.
     * <br><br>
     * This method can be used by all constructors and initializers.
     * Both by parametrized-method constructors and deserializers.
     * */
    protected void initTransients() {
        super.initTransients();
        this.drawables = new TreeMap<>();
    }

    /** Construct a new empty {@linkplain Scene}. */
    public Scene(){
        super();
    }

    /** Slow and strict method that registers all components that entity extends. */
    public void slowlyRegCompos(Entity entity) {
        super.slowlyRegCompos(entity);
        if (entity instanceof Drawable drawable) {
            this.drawables.computeIfAbsent(drawable.getLayer(), (_k)->new HashSet<>())
                    .add(drawable);
        }
    }

    /** Slow and strict method that unregisters all components given entity extends. */
    public void slowlyUnregCompos(Entity entity) {
        super.slowlyUnregCompos(entity);
        if (entity instanceof Drawable drawable) {
            this.drawables.get(drawable.getLayer())
                    .remove(drawable);
        }
    }

    // Processing collections

    /** @return a {@link DrawCommand} that draws this scene, when invoked */
    @Override
    public DrawCommand drawCommand() {
        ArrayList<DrawCommand> subcommands= new ArrayList<>();
        for (HashSet<Drawable> renderLayer : this.drawables.values()){
            for (Drawable drawable : renderLayer) {
                subcommands.add(drawable.drawCommand());
            }
        }
        return new DrawCommand(){
            @Override
            public void draw(Graphics graphics, ImageObserver observer) {
                for (var subcmd :
                        subcommands) {
                    subcmd.draw(graphics, observer);
                }
            }
            @Override
            public String toString(){
                return String.format( "Execute:  [  %s  ]",
                        subcommands
                                .stream()
                                .map(subcmd->subcmd.toString())
                                .collect(Collectors.joining(",  "))
                );
            }
        };
    }

    /** Allows to order this drawable.
     * <br><br>
     * This method can be useful when `<code>this</code>` is a part of other {@linkplain Scene}.
     * */
    @Override
    public RenderLayer getLayer() {
        return RenderLayer.BACKGROUND;
    }
}
