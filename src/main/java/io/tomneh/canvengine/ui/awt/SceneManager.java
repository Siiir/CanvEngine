package io.tomneh.canvengine.ui.awt;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;
import io.tomneh.canvengine.ui.awt.compos.Drawable;
import io.tomneh.canvengine.ui.awt.compos.Scene;

import io.tomneh.canvengine.util.SyncChannel;

/**
 * Abstraction over {@link Scene} that facilitates its usage and allows to extract or replace it.
 * <br><br>
 * It also facilitates maintenance and separates logic.
 */
public class SceneManager extends io.tomneh.canvengine.util.SceneManager{
    /**
     * @param scene Scene that is to be managed by constructed instance.
     * @param commandChannel Constructed instance
     *                        will be dependent on commands provided via this channel.
     * */
    public SceneManager(Scene scene, SyncChannel<Command> commandChannel) {
        super(scene, commandChannel);
    }

    /** Gets the scene managed by `this`. */
    @Override
    public Scene getScene() {
        return (Scene) this.scene;
    }


    /** Wraps a call to {@linkplain Drawable#drawCommand()} of internal scene. */
    public DrawCommand drawCommand(){
        return this.getScene().drawCommand();
    }
}
