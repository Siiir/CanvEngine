package io.tomneh.canvengine.exec.cmd;

import io.tomneh.canvengine.ui.awt.compos.Scene;
import io.tomneh.canvengine.util.SceneManager;

import java.io.*;

enum LateCmdOrder{
    PAUSE,
    SAVE,
    LOAD,
}

public interface OrdLateCmd extends Command, Comparable<OrdLateCmd>{
    LateCmdOrder ordinal();
    @Override
    default int compareTo(OrdLateCmd other){
        return this.ordinal().compareTo(other.ordinal());
    }

    record Pause() implements OrdLateCmd{
        @Override
        public LateCmdOrder ordinal() {
            return LateCmdOrder.PAUSE;
        }
        @Override
        public void exec(SceneManager ctx) {
            var scene= ctx.getScene();
            scene.paused= !scene.paused;
            System.err.printf("Game %s.\n", scene.paused? "paused":"unpaused");
        }
    }
    record Save(String saveName) implements OrdLateCmd{
        @Override
        public LateCmdOrder ordinal() {
            return LateCmdOrder.SAVE;
        }
        @Override
        public void exec(SceneManager ctx) {
            try {
                ObjectOutputStream oOS= new ObjectOutputStream(
                        new FileOutputStream(ctx.SAVE_DIR + this.saveName)
                );
                oOS.writeObject(ctx.getScene());
                oOS.close();
                System.err.printf("Succeeded at saving scene as \"%s\".\n", this.saveName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    record Load(String saveName) implements OrdLateCmd{
        @Override
        public LateCmdOrder ordinal() {
            return LateCmdOrder.LOAD;
        }
        @Override
        public void exec(SceneManager ctx) {
            try {
                ObjectInputStream oIS= new ObjectInputStream(
                        new FileInputStream(ctx.SAVE_DIR + this.saveName)
                );
                ctx.setScene( (Scene)oIS.readObject() );
                oIS.close();
                System.err.printf("Succeeded at loading scene named \"%s\".\n", this.saveName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

