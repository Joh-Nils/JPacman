package Entities;

import main.GamePanel;
import util.AssetPool;
import util.ImageTransform;

import java.awt.*;

import static util.getURL.getURL;

public class Ghost extends Entity{
    public boolean vulnerable = false;
    private GamePanel gp;

    public Ghost(GamePanel gp, char color, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;

        switch (color) {
            case 'R' -> spriteSheet = AssetPool.getSpriteSheet(getURL("/images/redGhost.png"),16,16);
            case 'G' -> spriteSheet = AssetPool.getSpriteSheet(getURL("/images/greenGhost.png"),16,16);
            case 'O' -> spriteSheet = AssetPool.getSpriteSheet(getURL("/images/orangeGhost.png"),16,16);
            case 'B' -> spriteSheet = AssetPool.getSpriteSheet(getURL("/images/blueGhost.png"),16,16);
        }

        hitBox = new Rectangle(0,0,16,16);
    }

    @Override
    public void update(float dt) {
        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer > spriteSheet.getSprites().length) walkingAnimationTimer -= spriteSheet.getSprites().length;

        if (!vulnerable) {
            hitBox.translate( (int) x / GamePanel.scale, (int) y / GamePanel.scale);
            gp.player.hitBox.translate( (int) gp.player.x / GamePanel.scale, (int) gp.player.y / GamePanel.scale);

            if (hitBox.intersects(gp.player.hitBox)) {
                //TODO: Hit
            }

            //CleanUp
            hitBox.translate( (int) -x / GamePanel.scale, (int) -y / GamePanel.scale);
            gp.player.hitBox.translate( (int) -gp.player.x / GamePanel.scale, (int) -gp.player.y / GamePanel.scale);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(spriteSheet.getSprite((int) walkingAnimationTimer),(int) x,(int) y,hitBox.width * GamePanel.scale, hitBox.height * GamePanel.scale, null);

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) (hitBox.x * GamePanel.scale + x), (int) (hitBox.y * GamePanel.scale + y), hitBox.width * GamePanel.scale, hitBox.height * GamePanel.scale);
        }
    }
}
