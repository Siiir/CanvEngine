package io.tomneh.canvengine.res.assets;

import io.tomneh.canvengine.res.AssetServ;
import io.tomneh.canvengine.res.ImgProperties;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageAsset implements Serializable {
    // Fields
    protected ImgProperties properties;
    transient protected BufferedImage refToCachedImg;
    // Getters
    public ImgProperties getProperties() {
        return properties;
    }
    public BufferedImage getRefToCachedImg() {
        return refToCachedImg;
    }
    // Setters
    public void changeProperties(ImgProperties newProperties) {
        this.properties = newProperties;
        this.refToCachedImg = AssetServ.getImage(this.properties);
    }
    // Constructors
    public ImageAsset(ImgProperties properties) {
        this.changeProperties(properties);
    }

    // Serialization methods
    private void writeObject(ObjectOutputStream oOS) throws IOException {
        oOS.defaultWriteObject();
    }
    private void readObject(ObjectInputStream oIS) throws IOException, ClassNotFoundException {
        oIS.defaultReadObject();
        this.refToCachedImg = AssetServ.getImage(properties);
    }
}

