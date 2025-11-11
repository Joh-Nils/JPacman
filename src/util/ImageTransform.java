package util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTransform {
    public static BufferedImage rotateImage(BufferedImage original, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        int width = original.getWidth();
        int height = original.getHeight();

        // Calculate new image size (bounding box of rotated image)
        double sin = Math.abs(Math.sin(angleRadians));
        double cos = Math.abs(Math.cos(angleRadians));
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        // Create new image
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g2d = rotated.createGraphics();

        // Rotate around the center of the new image
        g2d.translate((newWidth - width) / 2.0, (newHeight - height) / 2.0);
        g2d.rotate(angleRadians, width / 2.0, height / 2.0);
        g2d.drawRenderedImage(original, null);
        g2d.dispose();

        return rotated;
    }

    public static BufferedImage flipImage(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage flipped = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = flipped.createGraphics();

        g2d.drawImage(original, 0, 0, width, height,  // dest rect
                width, 0, 0, height,  // src rect reversed in X
                null);
        g2d.dispose();

        return flipped;
    }

    public static SpriteSheet rotateSpriteSheet(SpriteSheet original, double degrees) {
        SpriteSheet rotated = new SpriteSheet(original.getSprites().length, original.getSpriteWidth(), original.getSpriteHeight());

        for (int i = 0;i < rotated.getSprites().length;i++) {
            rotated.setSprite(rotateImage(original.getSprite(i),degrees),i);
        }

        return rotated;
    }
    public static SpriteSheet flipSpriteSheet(SpriteSheet original) {
        SpriteSheet flipped = new SpriteSheet(original.getSprites().length, original.getSpriteWidth(), original.getSpriteHeight());

        for (int i = 0; i < flipped.getSprites().length; i++) {
            flipped.setSprite(flipImage(original.getSprite(i)),i);
        }

        return flipped;
    }
}
