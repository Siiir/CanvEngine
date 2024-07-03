package io.tomneh.canvengine.examples.angry_slaw.gui;

import io.tomneh.canvengine.ui.swing.GameWin;
import io.tomneh.canvengine.ui.swing.SceneRenderer;

/** A window that contains game interface and scene. */
public class MainWin extends GameWin {
    public MainWin(){
        super();
        // Configuration
        {
            this.addTypicalCfg();
            this.setSize(1290, 687);
            this.setResizable(false);
        }
        // Adding widgets
        {
            this.addToolbar(new AngryToolbar(this.commandChannel));
            this.addSceneRenderer(new SceneRenderer());
        }
        this.activate();
    }
}
