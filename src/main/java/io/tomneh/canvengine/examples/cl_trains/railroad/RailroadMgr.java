package io.tomneh.canvengine.examples.cl_trains.railroad;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SceneManager;
import io.tomneh.canvengine.util.SyncChannel;

import java.util.concurrent.locks.ReentrantLock;

public class RailroadMgr extends SceneManager {
    final SyncChannel<Runnable> instrForUI;
    final ReentrantLock stdoutLock;

    /**
     * @param scene          Scene that is to be managed by constructed instance.
     * @param commandChannel Constructed instance
     *                       will be dependent on commands provided via this channel.
     * @param instrForUI
     * @param stdoutLock
     */
    public RailroadMgr(RailroadScene scene, SyncChannel<Command> commandChannel, SyncChannel<Runnable> instrForUI, ReentrantLock stdoutLock) {
        super(scene, commandChannel);
        this.instrForUI = instrForUI;
        this.stdoutLock = stdoutLock;
    }

    @Override
    public RailroadScene getScene(){
        return (RailroadScene) super.getScene();
    }
}
