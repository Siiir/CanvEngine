package io.tomneh.canvengine.ui.swing;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.exec.cmd.OrdLateCmd;
import io.tomneh.canvengine.util.SyncChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

/** A game toolbar.
 * <br><br>
 * Typically, having at least 3 buttons available: "Save", "Load", "Pause".
 * */
public class Toolbar extends JToolBar {

    /** A channel through, which toolbar can send commands directly to receiver.
     * */
    final protected SyncChannel<Command> commandChannel;

    /** The most basic {@linkplain Toolbar} constructor,
     *  which expects you only to specify where to send commands generated on user actions. */
    public Toolbar(SyncChannel<Command> commandChannel) {
        super();
        this.commandChannel= commandChannel;
        this.setFocusable(false); // So that attention doesn't drift away from `JFrame`.
    }

    /** Constructs a toolbar with 3 basic buttons: "Save", "Load", "Pause".
     * <br><br>
     * This can be helpful while creating a simple game or experimenting.
     * By default, "Save" & "Load" buttons will use "save0.ass" as save-file name.
     * */
    public static Toolbar with_defaults(SyncChannel<Command> commandChannel){
        return Toolbar.with_defaults(
                commandChannel,
                (tB,e) -> tB.commandChannel.push(new OrdLateCmd.Save("save0.ass")),
                (tB,e) -> tB.commandChannel.push(new OrdLateCmd.Load("save0.ass")),
                (tB,e) -> tB.commandChannel.push(new OrdLateCmd.Pause())
        );
    }

    /** Constructs a toolbar with 3 basic buttons: "Save", "Load", "Pause".
     * <br><br>
     * This can be helpful while creating a simple game or experimenting.
     * */
    public static Toolbar with_defaults(
            SyncChannel<Command> commandChannel,
            BiConsumer<Toolbar, ActionEvent> save,
            BiConsumer<Toolbar, ActionEvent> load,
            BiConsumer<Toolbar, ActionEvent> pause) {
        Toolbar instance = new Toolbar(commandChannel);
        instance.addDefaults(save, load, pause);
        return instance;
    }

    /** Adds three basic buttons that activate respective functions on event.
     * @param saveAction function assigned to "Save" button.
     * @param loadAction function assigned to "Load" button.
     * @param pauseAction function assigned to "Pause" button.
     * */
    public void addDefaults(BiConsumer<Toolbar, ActionEvent> saveAction,
                            BiConsumer<Toolbar, ActionEvent> loadAction,
                            BiConsumer<Toolbar, ActionEvent> pauseAction) {
        var thisToolbar= this;
        // Create the buttons and bind them to inline Action objects
        JButton saveButton = new JButton(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction.accept(thisToolbar, e);
            }
        });
        JButton loadButton = new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAction.accept(thisToolbar, e);
            }
        });
        JButton pauseButton = new JButton(new AbstractAction("Pause") {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseAction.accept(thisToolbar, e);
            }
        });

        // Add the buttons to the toolbar using the pushRight method.
        this.pushRight(saveButton);
        this.pushRight(loadButton);
        this.pushRight(pauseButton);
    }

    /** Adds provided component to the right of the toolbar.
     * <br><br>
     * Note: Before pushing to the right. The component
     * */
    public void pushRight(Component comp) {
        comp.setFocusable(false);
        this.add(comp);
    }
}
