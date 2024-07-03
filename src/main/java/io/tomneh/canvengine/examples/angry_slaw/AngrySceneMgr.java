package io.tomneh.canvengine.examples.angry_slaw;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SyncChannel;
import io.tomneh.canvengine.ui.awt.compos.Scene;
import io.tomneh.canvengine.ui.awt.SceneManager;

public class AngrySceneMgr extends SceneManager {
    /** Maximum accepted time between frames.
     * */
    public static final int MAX_MS_BETWEEN_FRAMES = 300;

    /**
     * @param scene          Scene that is to be managed by constructed instance.
     * @param commandChannel Constructed instance
     *                       will be dependent on commands provided via this channel.
     */
    public AngrySceneMgr(Scene scene, SyncChannel<Command> commandChannel) {
        super(scene, commandChannel);
    }

    /**
     * @param commandChannel Constructed instance
     *                       will be dependent on commands provided via this channel.
     */
    public AngrySceneMgr(SyncChannel<Command> commandChannel){
        this(new AngryScene(), commandChannel);
    }
}
