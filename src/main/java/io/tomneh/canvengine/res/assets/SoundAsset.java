package io.tomneh.canvengine.res.assets;

import io.tomneh.canvengine.res.AssetServ;
import io.tomneh.canvengine.res.BufferedSound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SoundAsset implements Serializable {
    // Fields
    protected String soundName;
    transient protected BufferedSound refToCachedSound;

    // Getters
    public String getSoundName() {
        return this.soundName;
    }

    public BufferedSound getRefToCachedSound() {
        return this.refToCachedSound;
    }

    // Setters
    public void changeSound(String soundName) {
        this.soundName = soundName;
        this.refToCachedSound = AssetServ.getSound(this.soundName);
    }

    // Constructors
    public SoundAsset(String soundName) {
        this.changeSound(soundName);
    }

    // Serialization methods
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        refToCachedSound = AssetServ.getSound(this.soundName);
    }
}
