package io.tomneh.canvengine.examples.angry_slaw.gui;

import io.tomneh.canvengine.exec.cmd.OrdLateCmd;
import io.tomneh.canvengine.util.SyncChannel;
import io.tomneh.canvengine.ui.swing.Toolbar;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AngryToolbar extends Toolbar {
    public AngryToolbar(SyncChannel commandChannel) {
        super(commandChannel);
        this.pushAllWidgets();
    }
    private void pushAllWidgets() {
        // Pushing buttons.

        // Save buttons
        this.pushRightSaveBtn("Save1", "Save1.ass");
        this.pushRightSaveBtn("Save2", "Save2.ass");
        // Pause button
        this.pushRightPauseBtn("Pause");
        // Load buttons
        this.pushRightLoadBtn("Load1", "Save1.ass");
        this.pushRightLoadBtn("Load2", "Save2.ass");
    }
    private void pushRightSaveBtn(String btnName, String saveName){
        // Bindings
        var thisToolbar= this;
        // Construct --> Push
        this.pushRight( new JButton( new AbstractAction(btnName) {
            @Override
            public void actionPerformed(ActionEvent _e) {
                thisToolbar.commandChannel.push(new OrdLateCmd.Save(saveName));
            }
        }));
    }
    private void pushRightLoadBtn(String btnName, String saveName){
        // Bindings
        var thisToolbar= this;
        // Construct --> Push
        this.pushRight( new JButton( new AbstractAction(btnName) {
            @Override
            public void actionPerformed(ActionEvent _e) {
                thisToolbar.commandChannel.push(new OrdLateCmd.Load(saveName));
            }
        }));
    }
    private void pushRightPauseBtn(String btnName){
        // Binding
        var thisToolbar= this;
        // Pushing
        this.pushRight(new JButton(new AbstractAction(btnName) {
            @Override
            public void actionPerformed(ActionEvent _e) {
                thisToolbar.commandChannel.push(new OrdLateCmd.Pause());
            }
        }));
    }
}
