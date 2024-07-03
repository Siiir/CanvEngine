package io.tomneh.canvengine.res;

import io.tomneh.canvengine.exec.ParallelExecutor;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class BufferedSound implements Serializable {
    protected byte[] inner;

    public BufferedSound(byte[] inner) {
        this.inner = inner;
    }

    public void play() throws JavaLayerException {
        InputStream iS= new ByteArrayInputStream(this.inner);
        var player= new Player(iS);
        player.play();
        player.close();
    }
    public void playUsingExecutor(){
        ParallelExecutor.exec(()-> {
            try {
                this.play();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void playInLoopUsingExecutor(){
        ParallelExecutor.exec(()-> {
            try {
                this.playInLoop();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void playInLoop() throws JavaLayerException{
        for(;;){this.play();}
    }
}