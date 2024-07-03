package io.tomneh.canvengine.examples.plain_tanks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Toolbar extends JToolBar {
    MainWindow rootWindow;
    private JButton btnLoad;
    private JButton btnSave;
    private JButton btnPause;

    public Toolbar(MainWindow rootWindow) {
        this.rootWindow= rootWindow;

        btnLoad = new JButton("Load");
        btnSave = new JButton("Save");
        btnPause = new JButton("Pause");

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Load: ");
                try {
                    { // This is elaborate fix of “toolbar events spoil key recorder” bug via window restart.
                        rootWindow.getScene().state.isPaused.set(true); // so that the “looping thread dies”
                        rootWindow.dispose(); // so that now useless window dies
                        new MainWindow(Res.loadScene());
                    }
                    System.out.println(" successful");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Save: ");
                try {
                    { // This is elaborate fix of “toolbar events spoil key recorder” bug via window restart.
                        Res.saveScene(rootWindow.getScene());
                        rootWindow.restartWinOnly();
                    }
                    System.out.println(" successful");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Pause = ");
                Scene scene = rootWindow.getScene();
                AtomicBoolean isPaused = scene.state.isPaused;
                boolean isPausedNow= isPaused.get();
                if(isPausedNow){
                    { // This is elaborate fix of “toolbar events spoil key recorder” bug via window restart.
                        // swap `.isPaused` member in `scene`, so that the „looping thread doesn't see new `.isPaused.get()` value.
                        // unpausing at the same time, but only on `scene.state` level
                        scene.state.isPaused = new AtomicBoolean(false);
                        // Now the “looping thread” doesn't know the game is unpaused and die as it believes it is useless.
                        // But `rootWindow` is ok. without looping thread if and only if game is paused.
                        rootWindow.dispose();
                        // It is unpaused, so we need new looping thread and a new window
                        new MainWindow(scene);
                    }
                }else{ // pause
                    isPaused.set(true);
                }
                System.out.println(!isPausedNow);
            }
        });

        this.add(btnLoad);
        this.add(btnSave);
        this.add(btnPause);
    }
}
