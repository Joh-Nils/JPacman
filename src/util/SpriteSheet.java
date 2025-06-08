package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class SpriteSheet {

    private BufferedImage image;

    private BufferedImage[] sprites;
    public SpriteSheet(URL url, int SpriteWidth, int SpriteHeight) {
        assert url != null || SpriteWidth != 0|| SpriteHeight != 0 : "Variables cant be null";

        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public SpriteSheet(int size) {
        sprites = new BufferedImage[size];
    }

    public BufferedImage getSprite(int index) {
        return sprites[index];
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public BufferedImage getOriginal() {
        return this.image;
    }

    public void setSprite(BufferedImage image, int index) {
        sprites[index] = image;
    }
}
