package Entities;

import main.GamePanel;
import util.AssetPool;

import java.awt.*;
import java.awt.image.BufferedImage;

import static util.getURL.getURL;

public class Coin extends Entity {
    private GamePanel gp;
    private boolean collected = false;
    private final boolean big;

    public Coin(GamePanel gp, boolean big, int x, int y) {
        this.gp = gp;
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

        //Player collection
        hitBox.translate((int) (x), (int) (y));
        gp.player.hitBox.translate((int) (gp.player.x), (int) (gp.player.y));

        if (gp.player.hitBox.intersects(hitBox)) {
            use();
        }

        //CleanUp
        hitBox.translate((int) (-x), (int) (-y));
        gp.player.hitBox.translate((int) (-gp.player.x), (int) (-gp.player.y));
    }

    @Override
    public void draw(Graphics2D g) {
        if (collected) return;

        BufferedImage image = spriteSheet.getSprite((int) walkingAnimationTimer);
        g.drawImage(image,(int) (x * GamePanel.scale),(int) (y * GamePanel.scale), (int) (image.getWidth() * GamePanel.scale), (int) (image.getHeight() * GamePanel.scale), null);

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) ((hitBox.x + x) * GamePanel.scale), (int) ((hitBox.y + y) * GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));
        }
    }

    public void use() {
        collected = true;
        gp.player.Score += big ? 0 : 1;
    }
}
