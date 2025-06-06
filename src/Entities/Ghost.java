package Entities;

import main.GamePanel;
import util.AssetPool;
import util.ImageTransform;

import java.awt.*;

import static util.getURL.getURL;

public class Ghost extends Entity{
    public boolean vulnerable = false;
    private GamePanel gp;

    public boolean started = false;

    public char direction = ' ';

    private int speed;

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

        speed = 5;

        hitBox = new Rectangle(0,0,16,16);
    }

    @Override
    public void update(float dt) {
        if (!started) return;

        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer > spriteSheet.getSprites().length) walkingAnimationTimer -= spriteSheet.getSprites().length;

        direction = gp.astar.getDirection();

        move(dt);

        if (!vulnerable) {
            hitBox.translate((int) (x / GamePanel.scale), (int) (y / GamePanel.scale));
            gp.player.hitBox.translate((int) (gp.player.x / GamePanel.scale), (int) (gp.player.y / GamePanel.scale));

            if (hitBox.intersects(gp.player.hitBox)) {
                //TODO: Hit better
                gp.player.Lives--;
                gp.reset();
            }

            //CleanUp
            hitBox.translate((int) (-x / GamePanel.scale), (int) (-y / GamePanel.scale));
            gp.player.hitBox.translate((int) (-gp.player.x / GamePanel.scale), (int) (-gp.player.y / GamePanel.scale));
        }
    }

    public void stop() {
        started = false;
    }

    private void move(float dt) {

        //TODO Collision Handling
        switch (direction) {
            case 'U' -> {
                y -= speed * dt;
            }
            case 'L' -> {
                x -=  speed * dt;
            }
            case 'D' -> {
                y += speed * dt;
            }
            case 'R' -> {
                x += speed * dt;
            }

            default -> {
                assert false : "Unknown Direction Key: '" + direction + "'";
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(spriteSheet.getSprite((int) walkingAnimationTimer),(int) x,(int) y, (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) (hitBox.x * GamePanel.scale + x), (int) (hitBox.y * GamePanel.scale + y), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));
        }
    }
}
