package io.tomneh.canvengine.util;

import io.tomneh.canvengine.Consts;
import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.exec.cmd.OrdLateCmd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import io.tomneh.canvengine.Consts;

/**
 * Abstraction over {@link Scene} that facilitates its usage and allows to extract or replace it.
 * <br><br>
 * It also facilitates maintenance and separates logic.
 */
public class SceneManager {

    // Shared consts

    /** Path leading to director containing game saves. */
    final static public String SAVE_DIR = Consts.GAME_DIR + "saves/";


    // Instance finals

    /** Communication channel by which `this` receives commands from UIs.
     *  <br><br>
     *  There can be more than one UI running & sending commands from arbitrary thread.
     *  */
    protected final SyncChannel<Command> commandChannel;

    /** Buffer for commands, which are created and pushed in arbitrarily before exec-commands phase.
     *  Then processed all together.
     * @see Command
     * */
    protected final ArrayList<Command> commands;

    /** Stores keys that are currently being pressed by the app user.
     * <br><br>
     * Key recording mechanism might be optimized to store only keys that matter in the game.
     */
    public final HashSet<Integer> currentlyPressedKeys= new HashSet<>();


    // Instance vars
    /** The scene that is managed by `this`.*/
    protected Scene scene;

    /** Time delta in nanoseconds.
     * @see Consts#MIN_NS_BETWEEN_FRAMES
     * */
    long tDeltaInNs = 0;

    /** Time delta in seconds. */
    float tDeltaInSec = 0.0f;



    // Constructors
    /**
     * @param scene Scene that is to be managed by constructed instance.
     * @param commandChannel Constructed instance
     *                        will be dependent on commands provided via this channel.
     * */
    public SceneManager(Scene scene, SyncChannel<Command> commandChannel) {
        // Finals
        this.commandChannel= commandChannel;
        this.commands= new ArrayList<>();
        // Vars
        this.setScene(scene);
    }



    // Getters
    /** Trivial getter of the value.
     * <br>
     * Doesn't perform any calculations.
     * */
    public float getTDeltaInSec(){
        return this.tDeltaInSec;
    }
    /** Trivial getter of the value.
     * <br>
     * Doesn't perform any calculations.
     * */
    public long getTDeltaInNs(){
        return this.tDeltaInNs;
    }
    /** Gets the {@link Scene} managed by `this`. */
    public Scene getScene() {
        return scene;
    }
    /** Reference getters for command channel used by `this`. */
    public SyncChannel<Command> getCommandChannel() {
        return commandChannel;
    }


    // Setters

    /** Sets all internal time deltas
     *   (<code>this.{@link #tDeltaInNs}, this.{@link #tDeltaInSec}, ...</code>)
     * */
    public void setTDelta(long nanoseconds){
        this.tDeltaInNs= nanoseconds;
        this.tDeltaInSec= (float) nanoseconds / Consts.SEC_TO_NS_MULTIPLIER;
    }

    /** Sets the {@link Scene} managed by `this`. */
    public void setScene(Scene scene) {
        this.scene = scene;
    }


    // Sequence processors

    /** Wraps call to {@link Scene#update(SceneManager)} of internal scene.*/
    public void updateScene(){
        this.scene.update(this);
    }

    /**
     * Moves all commands from internal synchronized command channel {@link #commandChannel}
     *  to not-sync. internally owned command buffer {@link #commands}.
     * Then processes all commands in internal buffer. Empties buffer afterwards.
     * */
    public void readThanExecCmds(){
        // Load command list. Empty mutex channel.
        this.commandChannel.takeAll(this.commands);
        // Prepare space for commands to procrastinate.
        var lateCmds= new TreeSet<OrdLateCmd>();
        // Process all commands in command list.
        for (Command cmd : this.commands) {
            if (cmd instanceof OrdLateCmd lateCmd){
                lateCmds.add(lateCmd); // procrastinate
            }else{
                cmd.exec(this); // exec now
            }
        }
        this.commands.clear(); // End with past commands. Make space for future ones.
        // Finally, process late commands in their predefined natural order.
        for (OrdLateCmd lateCmd :
                lateCmds) {
            lateCmd.exec(this);
        }
    }
}
