package io.tomneh.canvengine.examples.angry_slaw;

import io.tomneh.canvengine.Consts;
import io.tomneh.canvengine.examples.angry_slaw.gui.MainWin;
import io.tomneh.canvengine.ui.swing.SceneRenderer;

import javax.swing.*;

public class MainLoop {
    public static void main(String[] args) {
        // `final` bindings
        final SceneRenderer sceneRenderer;
        final io.tomneh.canvengine.examples.angry_slaw.AngrySceneMgr sceneManager;
        {
            var mainWin= new MainWin();
            sceneRenderer= mainWin.getSceneRenderer();
            sceneManager = new io.tomneh.canvengine.examples.angry_slaw.AngrySceneMgr( mainWin.commandChannel );
        }

        // Main loop
        for(byte modCounter= 0;;modCounter++){
            long startTimeInNs= System.nanoTime();
            // Scene update
            sceneManager.updateScene();
            sceneManager.readThanExecCmds();
            if (sceneManager.getScene().paused){
                // Don't do any entity graphic processing
            }else{
                // Some graphic processing
                var drawCmd= sceneManager.drawCommand();
                // Rendering. In future there's gonna be some
                // measures against over-queuing `Runnable` invocations.
                SwingUtilities.invokeLater( () -> {
                    sceneRenderer.currentDrawCommand= drawCmd;
                    sceneRenderer.repaint();
                } );
            }
            long endTimeInNS= System.nanoTime();
            long elapsedTimeInNs= endTimeInNS -startTimeInNs;
            if (modCounter==0) System.err.printf("Sample update of logic took: %dms.\n",
                    elapsedTimeInNs / Consts.MS_TO_NS_MULTIPLIER);
            // Sleeping
            long sleepTimeInMs= Consts.MIN_MS_BETWEEN_FRAMES -(elapsedTimeInNs/Consts.MS_TO_NS_MULTIPLIER);
            if (sleepTimeInMs < 0){
                sceneManager.setTDelta(elapsedTimeInNs);
                System.err.printf(
                        "WARN: Detected update that took %d mili seconds longer then expected,"
                        +" but FPS stabilizer is not implemented yet.\n",
                        -sleepTimeInMs
                );
            }else{
                sceneManager.setTDelta( Consts.MIN_NS_BETWEEN_FRAMES );
                try {
                    Thread.sleep(sleepTimeInMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}