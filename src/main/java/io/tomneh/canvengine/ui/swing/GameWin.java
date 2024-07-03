package io.tomneh.canvengine.ui.swing;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SyncChannel;
import io.tomneh.canvengine.ui.awt.KeyInterpreter;

import javax.swing.*;
import java.awt.*;

/** A window that is to contain game GUI. */
public class GameWin extends JFrame {

    // Instance fields.

    /** The internal channel that is used to send user commands to executor.
     * <br><br>
     * The general idea is that GUI events like button clicks
     *  are transformed into their command counterparts.
     * Commands are then transferred to executor like {@link io.tomneh.canvengine.ui.awt.SceneManager}.
     *  */
    public final SyncChannel<Command> commandChannel;

    // Getters
    public KeyInterpreter getKeyRecorder(){
        return (KeyInterpreter) this.getKeyListeners()[0];
    }
    public Toolbar getToolbar(){
        return (Toolbar) this.getContentPane().getComponent(0);
    }
    public SceneRenderer getSceneRenderer(){
        return (SceneRenderer) this.getContentPane().getComponent(1);
    }

    // Setters
    /**
     * Sets default window configuration like name & size.
     * <br><br>
     * This default configuration is good for testing and experimenting purposes.
     * Note: It doesn't add any widgets.
     * @see #fillWithDefaultWidgets()
     * @see #addTypicalCfg()
     */
    protected void setDefaultCfg() {
        this.setTitle("Experiments");
        this.setSize(500, 350);
    }

    // Constructors

    /** Constructs and empty window.
     */
    public GameWin() { this(new SyncChannel<>()); }

    /** Constructs an empty window with a concrete command channel assigned. */
    public GameWin(SyncChannel<Command> commandChannel) throws HeadlessException {
        super();
        this.commandChannel = commandChannel;
    }

    /** Constructs an empty window with a title and a concrete command channel assigned. */
    public GameWin(String title, SyncChannel<Command> commandChannel) throws HeadlessException {
        super(title);
        this.commandChannel = commandChannel;
    }

    /** Constructs a new window with default widgets that is ready to use or test. */
    public GameWin with_defaults() {
        var instance= new GameWin();
        instance.setDefaultCfg();

        instance.fillWithDefaultWidgets();
        instance.activate();

        return instance;
    }

    // Adders (Extenders)

    // Adders for logical objects like configuration.

    /** Adds & sets typical configuration that I use in most of my swing games.
     * <br><br>
     * Note: It shouldn't be called more than once.
     * <h3>Func. description:</h3>
     * <code>
     *         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     *         this.addDefaultKeyRecorder();
     * </code>
     * @see #setDefaultCfg()
     * */
    protected void addTypicalCfg() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addDefaultKeyRecorder();
    }

    /** Registers `<code>new {@link KeyInterpreter}(this.{@link #commandChannel})</code>`. */
    protected void addDefaultKeyRecorder(){
        this.addKeyListener(new KeyInterpreter(this.commandChannel));
    }

    // Adders for widgets.

    /** Fills `this` with default widgets by invoking its `addDefault*` methods.
     * <br><br>
     * This function is useful for experimenting.
     * Some widgets like buttons might have some default functionality.
     * Some default to trace messages.
     * Other have no functionality in their default form.
     * */
    protected void fillWithDefaultWidgets() {
        this.addDefaultToolbar(this.commandChannel);
        this.addDefaultSceneRenderer();
    }

    /** Fills `this` with all basic widgets. */
    protected void fillWithWidgets(Toolbar toolbar, SceneRenderer sceneRenderer){
        this.addToolbar(toolbar);
        this.addSceneRenderer(sceneRenderer);
    }
    /** Equivalent to: <br>
     *  <code>this.{@link #addToolbar}( {@link Toolbar#with_defaults}(commandChannel) );</code> */
    protected void addDefaultToolbar(SyncChannel commandChannel) {
        this.addToolbar( Toolbar.with_defaults(commandChannel) );
    }

    /** Adds toolbar widget to the top of the GUI. */
    protected void addToolbar(Toolbar toolbar){
        this.add(toolbar, BorderLayout.PAGE_START);
    }
    /** Adds <code>new {@link SceneRenderer}()</code>.
     * <br><br>
     * This function is useful for experimenting.
     */
    protected void addDefaultSceneRenderer() {
        this.addSceneRenderer(new SceneRenderer());
    }

    /** Adds {@link SceneRenderer} to the bottom-center of the GUI. */
    protected void addSceneRenderer(SceneRenderer sceneRenderer){
        this.add(sceneRenderer, BorderLayout.CENTER);
    }

    // Other

    /** Its activating function from gui user perspective.
     * <br><br>
     * It makes window both visible and interactive.
     * */
    protected void activate() {
        this.setFocusable(true);
        this.setVisible(true);
    }
}
