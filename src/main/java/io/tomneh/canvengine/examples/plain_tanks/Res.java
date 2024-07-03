package io.tomneh.canvengine.examples.plain_tanks;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.NoSuchElementException;

class ResNotFoundException extends NoSuchElementException{
    public ResNotFoundException(String errMsg) {
        super(errMsg);
    }
}

public class Res {
    private static final String assetDir = "./assets";
    private static final File gameSavePath = new File(assetDir + "/save.tanks_save");

    private static final String texturesDirPath= assetDir +"/textures";
    private static final String soundsDirPath= assetDir +"/sounds";

    private static final HashMap<String, BufferedImage> textures= new HashMap<>();
    static BufferedImage getTexture(String name){
        // Get cache if present.
        BufferedImage texture= textures.get(name);
        // Return cached.
        if (texture!=null){
            return texture;
        }
        // Else find it in asset folder.
        File texturePath= new File(texturesDirPath+"/"+name+".png");
        // Error short circuit.
        if (!texturePath.exists()){
            throw new ResNotFoundException(
                    String.format("Couldn't find texture with name \"%s.\"", name)
                            + String.format(" It was present neither in cache nor in \"%s\".", texturePath.getAbsolutePath())
            );
        }
        try{
            texture= ImageIO.read(texturePath);
        }catch (IOException iOE){
            throw new RuntimeException(iOE);
        }
        // Cache & return
        textures.put(name, texture);
        return texture;
    }
    static void playSoundAmbiently(String soundName){
        new Thread( new Runnable() {
            @Override
            public void run() {
                var player= Res.getSoundPlayer(soundName);
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
                player.close();
            }
        } ).start();
    }
    static Player getSoundPlayer(String name){
        FileInputStream fis = null;
        String soundPath= soundsDirPath+"/"+name+".mp3";
        try {
            fis = new FileInputStream(soundPath);
        } catch (FileNotFoundException e) {
            throw new ResNotFoundException(
                    String.format("Couldn't find sound \"%s\".", soundPath)
            );
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            Player player = new Player(bis);
            return player;
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    static void saveScene(Scene scene) throws IOException {
        ObjectOutputStream saveStream= new ObjectOutputStream(
                new FileOutputStream(gameSavePath)
        );

        saveStream.writeObject(scene);

        saveStream.close();
    }

    static Scene loadScene() throws IOException, ClassNotFoundException {
        ObjectInputStream saveStream= new ObjectInputStream(
                new FileInputStream(gameSavePath)
        );

        Scene loadedScene= (Scene) saveStream.readObject();

        saveStream.close();

        return loadedScene;
    }

}