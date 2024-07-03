package io.tomneh.canvengine.examples.plain_tanks;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public class MainWindow extends JFrame {
    HashSet<Integer> pressedKeys = new HashSet<>();

    public MainWindow(){
        this(new Scene());
    }
    public MainWindow(Scene scene){
        super("Tanks");

        { // Pushing widgets.
            this.setLayout(new BorderLayout());
            this.getContentPane().add(new Toolbar(this), BorderLayout.NORTH);
            this.getContentPane().add(scene, BorderLayout.CENTER);
        }
        this.addKeyRecorder();
        { // Presetting behaviour
            this.setSize(1930, 1040);
            this.setResizable(false);

            this.setFocusable(true);
            this.setVisible(true);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        { // Game loop
            AtomicBoolean isPaused= scene.state.isPaused;
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    for(;!isPaused.get();) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                scene.updateFrame();
                            }
                        });
                        try {
                            Thread.sleep(18);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            })).start();
        }
    }

    public Scene getScene(){
        return (Scene) this.getContentPane().getComponent(1);
    }

    public void setScene(Scene newScene){
        this.getContentPane().remove(1);
        this.getContentPane().add(newScene, 1);
        this.validate();
    }

    public void restartWinOnly(){
        Scene oldScene= this.getScene(); // So that old scene is preserved
        boolean isPausedNow= oldScene.state.isPaused.get(); // So that its real `state.isPaused` is preserved
        oldScene.state.isPaused.set(true); // So that the looping thread dies
        this.dispose(); // So that the window dies.

        oldScene.state.isPaused= new AtomicBoolean(isPausedNow); // To make sure that looping thread notices it must die.
        new MainWindow(oldScene);
    }
    void addKeyRecorder() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                System.err.println("Noticed key "+e.getKeyCode()+" being pressed.");
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.err.println("Noticed key "+e.getKeyCode()+" being released.");
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

}
