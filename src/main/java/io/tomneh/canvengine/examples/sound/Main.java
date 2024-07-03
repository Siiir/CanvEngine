package io.tomneh.canvengine.examples.sound;

import io.tomneh.canvengine.res.AssetServ;
import io.tomneh.canvengine.exec.ParallelExecutor;
import javazoom.jl.decoder.JavaLayerException;

public class Main {
    public static void main(String[] args) throws JavaLayerException {
        AssetServ.getSound("effects/SteveOOFSound").playUsingExecutor();
    }
}
