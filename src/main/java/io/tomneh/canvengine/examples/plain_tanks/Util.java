package io.tomneh.canvengine.examples.plain_tanks;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Util {
    // https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
    public static BufferedImage rotated(BufferedImage img, double rotInRadians)
    {
        // Getting Dimensions of image
        int width = img.getWidth();
        int height = img.getHeight();

        // Creating a new buffered image
        BufferedImage newImage = new BufferedImage(
                img.getWidth(), img.getHeight(), img.getType());

        // creating Graphics in buffered image
        Graphics2D g2 = newImage.createGraphics();

        // Rotating image by degrees using toradians()
        // method
        // and setting new dimension t it
        g2.rotate(rotInRadians, width / 2,
                height / 2);
        g2.drawImage(img, null, 0, 0);

        // Return rotated buffer image
        return newImage;
    }
    // https://stackoverflow.com/questions/9417356/bufferedimage-resize
    public static BufferedImage resized(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}