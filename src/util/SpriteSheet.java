package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class SpriteSheet {

    private BufferedImage image;

    private BufferedImage[] sprites;

    private final HashMap<SpriteSheet, SpriteSheet> masks = new HashMap<>();

    private final int SpriteWidth;
    private final int SpriteHeight;


    public SpriteSheet(URL url, int SpriteWidth, int SpriteHeight) {
        assert url != null && SpriteWidth != 0 && SpriteHeight != 0 : "Variables cant be null";

        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.SpriteWidth = SpriteWidth;
        this.SpriteHeight = SpriteHeight;

        setup();
    }

    private SpriteSheet(SpriteSheet parent, SpriteSheet mask) {
        assert parent != null && mask != null: "Variables cant be null";
        assert parent.SpriteWidth == mask.SpriteWidth && parent.SpriteHeight == mask.SpriteHeight && parent.image.getWidth() == mask.image.getWidth() && parent.image.getHeight() == mask.image.getHeight() : "Mask cannot be applied to this SpiteSheet";

        image = new BufferedImage(parent.image.getWidth(), parent.image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int parentRGB = parent.image.getRGB(x, y);
                int maskRGB = mask.image.getRGB(x, y);

                Color newColor = getColor(parentRGB, maskRGB);
                image.setRGB(x, y, newColor.getRGB());
            }
        }

        this.SpriteWidth = parent.SpriteWidth;
        this.SpriteHeight = parent.SpriteHeight;

        setup();
    }

    private void setup() {
        int count = (image.getWidth() / SpriteWidth) * (image.getHeight() / SpriteHeight);

        sprites = new BufferedImage[count];

        int imageWidth = image.getWidth();

        int x = 0;
        int y = 0;
        int i = 0;
        while (i < count) {
            sprites[i] = image.getSubimage(x * SpriteWidth, y * SpriteHeight, SpriteWidth, SpriteHeight);
            i++;
            x++;

            if (x * SpriteWidth >= imageWidth) {
                x = 0;
                y++;
            }
        }
    }

    private static Color getColor(int parentRGB, int maskRGB) {
        Color c1 = new Color(parentRGB, true);
        Color c2 = new Color(maskRGB, true);

        int r = (c1.getRed()   * c2.getRed())   / 255;
        int g = (c1.getGreen() * c2.getGreen()) / 255;
        int b = (c1.getBlue()  * c2.getBlue())  / 255;

        int a = (c1.getAlpha() * c2.getAlpha()) / 255;

        return new Color(r, g, b, a);
    }

    public SpriteSheet(int size, int SpriteWidth, int SpriteHeight) {
        sprites = new BufferedImage[size];
        this.SpriteWidth = SpriteWidth;
        this.SpriteHeight = SpriteHeight;
    }

    public BufferedImage getSprite(int index) {
        return sprites[index];
    }

    public void registerMask(SpriteSheet mask) {
        masks.put(mask, new SpriteSheet(this, mask));
    }

    public SpriteSheet getMasked(SpriteSheet mask) {
        return this.masks.get(mask);
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public BufferedImage getOriginal() {
        return this.image;
    }

    public int getSpriteWidth() {
        return SpriteWidth;
    }
    public int getSpriteHeight() {
        return SpriteHeight;
    }

    public void setSprite(BufferedImage image, int index) {
        sprites[index] = image;
    }
}
