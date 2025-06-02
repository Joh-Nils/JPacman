package Entities;

import main.GamePanel;
import util.AssetPool;

import java.awt.*;
import java.awt.image.BufferedImage;

import static util.getURL.getURL;

public class Coin extends Entity {
    private boolean collected = false;
    private final boolean big;

    public Coin(boolean big, int x, int y) {
        this.big = big;
        this.x = x;
        this.y = y;

        if (big) {
            spriteSheet = AssetPool.getSpriteSheet(getURL("/images/BigCoin.png"),16,16);
            hitBox = new Rectangle(4,4,8,8);
        }
        else {
            spriteSheet = AssetPool.getSpriteSheet(getURL("/images/Coin.png"),16,16);
            hitBox = new Rectangle(5,5,6,6);
        }

    }

    @Override
    public void update(float dt) {
        if (collected) return;

        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer > spriteSheet.getSprites().length) walkingAnimationTimer -= spriteSheet.getSprites().length;
    }

    @Override
    public void draw(Graphics2D g) {
        if (collected) return;

        BufferedImage image = spriteSheet.getSprite((int) walkingAnimationTimer);
        g.drawImage(image,(int) x,(int) y,image.getWidth() * GamePanel.scale, image.getHeight() * GamePanel.scale, null);

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) (hitBox.x * GamePanel.scale + x), (int) (hitBox.y * GamePanel.scale + y), hitBox.width * GamePanel.scale, hitBox.height * GamePanel.scale);
        }
    }

    public int use() {
        collected = true;
        return big ? 0 : 1;
    }
}
