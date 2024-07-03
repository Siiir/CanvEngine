package io.tomneh.canvengine.res;

import io.tomneh.canvengine.not_mine.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.NoSuchElementException;

import io.tomneh.canvengine.Consts;

class AssetNotFoundException extends NoSuchElementException {
    public AssetNotFoundException(String errMsg) {
        super(errMsg);
    }
}

public class AssetServ {
    static final private String DIR = Consts.GAME_DIR +"assets/";
    static final private String SOUND_DIR = DIR +"sounds/";
    static final private String IMAGE_DIR = DIR +"images/";
    static final private HashMap<String, BufferedImage> images= new HashMap<>();
    static final private HashMap<ImgProperties, BufferedImage> transformedImages= new HashMap<>();
    static final private HashMap<String, BufferedSound> sounds= new HashMap<>();

    public static BufferedImage getImage(String name){
        // Get from cache if present.
        BufferedImage image= images.get(name);
        // Return cached.
        if (image!=null){
            return image;
        }

        // Else find it in asset folder.
        File imagePath= new File(IMAGE_DIR +"/"+name+".png");
        // Error short circuit.
        if (!imagePath.exists()){
            throw new AssetNotFoundException(
                    String.format("Couldn't find image with name \"%s.\"", name)
                            + String.format(" It was present neither in cache nor in \"%s\".", imagePath.getAbsolutePath())
            );
        }

        try{
            image= ImageIO.read(imagePath);
        }catch (IOException iOE){
            throw new RuntimeException(iOE);
        }
        // Cache & return
        images.put(name, image);
        return image;
    }
    public static BufferedImage getImage(ImgProperties properties) {
        // Get from cache if present.
        BufferedImage image = transformedImages.get(properties);
        // Return cached.
        if (image != null) {
            return image;
        }

        // Get raw version of desired image.
        image = getImage(properties.name());
        // Apply transformations
        image = Image.rotated(image, Math.toRadians(properties.rotInDegrees()));
        image = Image.resized(image, properties.width(), properties.height());

        // Cache & return
        transformedImages.put(properties, image);
        return image;
    }


    public static BufferedSound getSound(String name){
        // Get from cache if present.
        BufferedSound sound = sounds.get(name);
        // Return cached.
        if (sound != null) {
            return sound;
        }

        // Else find it in asset folder.
        System.err.println(SOUND_DIR);
        File soundFile = new File(SOUND_DIR + name + ".mp3");
        // Error short circuit.
        if (!soundFile.exists()) {
            throw new AssetNotFoundException(
                    String.format("Couldn't find sound with name \"%s.\"", name)
                            + String.format(" It was present neither in cache nor in \"%s\".", soundFile.getAbsolutePath())
            );
        }

        byte[] soundBytes;
        try {
            soundBytes = Files.readAllBytes(soundFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sound = new BufferedSound(soundBytes);
        // Cache & return
        sounds.put(name, sound);
        return sound;
    }

}