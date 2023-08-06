package io.github.itzispyder.clickcrystals.data.pixelart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageEditor {

    public static final int MAX_WIDTH = 128;
    public static final int MAX_HEIGHT = 64;
    private BufferedImage image;

    public ImageEditor(BufferedImage image) {
        this.image = image;
    }

    public static ImageEditor openFromUrl(URL url) throws IOException {
        InputStream is = url.openStream();
        BufferedImage bi = ImageIO.read(is);
        return new ImageEditor(bi);
    }

    public static ImageEditor openFromImage(BufferedImage image) {
        return image != null ? new ImageEditor(image) : null;
    }

    public void scaleToBounds() {
        if (image.getWidth() > MAX_WIDTH) {
            double r = MAX_WIDTH / (double)image.getWidth();
            int height = (int)(image.getHeight() * r);
            this.scaleToCustom(MAX_WIDTH, height);
        }
        else if (image.getHeight() > MAX_HEIGHT) {
            double r = MAX_HEIGHT / (double)image.getHeight();
            int width = (int)(image.getWidth() * r);
            this.scaleToCustom(width, MAX_HEIGHT);
        }
    }

    public void scaleToCustom(int customWidth, int customHeight) {
        image = bufferImage(image.getScaledInstance(customWidth, customHeight, Image.SCALE_FAST));
    }

    public BufferedImage bufferImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        BufferedImage bImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)bImg.getGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bImg;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getArea() {
        return getWidth() * getHeight();
    }
}
