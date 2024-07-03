package io.tomneh.canvengine.ui.swing;

import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;
import io.tomneh.canvengine.ui.awt.cmd.DrawNothing;

import io.tomneh.canvengine.ui.awt.SceneManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/** Use to draw a scene on it using {@link DrawCommand}.
 * <br><br>
 * In fact, you can render anything on it using {@link DrawCommand}.
 * <ol>To achieve that:
 * <li>Set the {@link #currentDrawCommand} to a {@link DrawCommand} of your choice.</li>
 * <li>Trigger repainting using {@link #repaint()}</li>
 * </ol>
 * */
public class SceneRenderer extends JPanel{

    /** All {@link #repaint()}s use it to draw.
     * <br><br>
     * <h3>Example usage:</h3>
     * <code>
     *     {@linkplain SceneRenderer} renderer = ...;<br>
     *     {@link SceneManager} sceneManager = ...;<br>
     *     renderer.{@linkplain #currentDrawCommand} = sceneManager.{@link SceneManager#drawCommand()};<br>
     *     renderer.{@link #repaint()};<br>
     * </code>
     * */
    public DrawCommand currentDrawCommand= new DrawNothing();

    /** Paints this by invoking {@link DrawCommand#draw(Graphics, ImageObserver)} of
     *  {@link #currentDrawCommand}. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.currentDrawCommand.draw(g, null);
    }
}
