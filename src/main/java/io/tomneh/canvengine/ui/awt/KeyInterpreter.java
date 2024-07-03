package io.tomneh.canvengine.ui.awt;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SyncChannel;
import io.tomneh.canvengine.exec.cmd.key.KeyboardPress;
import io.tomneh.canvengine.exec.cmd.key.KeyboardRelease;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** Projects {@link KeyEvent}s to their {@link Command} counterparts.
 * <br><br>
 * For each {@link KeyEvent}: <ol>
 *   <li>Intercepts it.</li>
 *   <li>If event is to be ignored method shortcuts by returning.</li>
 *   <li>Transforms event to {@link Command} counterpart.</li>
 *   <li>Sends command using {@link #commandChannel} or not.</li>
 * </ol>
 * */
public class KeyInterpreter extends KeyAdapter {

    /** Through this channel all commands are sent to the receiver (executor). */
    protected final SyncChannel<Command> commandChannel;

    /** @param commandChannel a channel,
     *                         via which all commands are sent to the receiver (executor) */
    public KeyInterpreter(SyncChannel<Command> commandChannel) {
        super();
        this.commandChannel = commandChannel;
    }

    /** <ol>
     *      <li>Intercepts press event.</li>
     *      <li>If this even can be treated as command.</li> <ol>
     *          <li>Transforms to command.</li>
     *          <li>Pushes it to {@link #commandChannel}.</li>
     *      </ol>
     * </ol> */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        this.commandChannel.push(new KeyboardPress(keyCode));
    }


    /** <ol>
     *      <li>Intercepts release event.</li>
     *      <li>If this even can be treated as command.</li> <ol>
     *          <li>Transforms to command.</li>
     *          <li>Pushes it to {@link #commandChannel}.</li>
     *      </ol>
     * </ol> */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        this.commandChannel.push(new KeyboardRelease(keyCode));
    }
}
