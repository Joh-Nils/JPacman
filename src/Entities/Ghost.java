package Entities;

import Scenes.PlayingScene;
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

    public char color = ' ';

    private int speed;

    public Ghost(GamePanel gp, char color, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.color = color;

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
        if (walkingAnimationTimer >= spriteSheet.getSprites().length) walkingAnimationTimer = walkingAnimationTimer % spriteSheet.getSprites().length;

        direction = gp.astar.getDirection();

        if (direction != ' ') move(dt);

        if (!vulnerable) {
            hitBox.translate((int) (x), (int) (y));
            gp.player.hitBox.translate((int) (gp.player.x), (int) (gp.player.y));

            if (hitBox.intersects(gp.player.hitBox)) {
                //TODO: Hit better
                gp.player.Lives--;
                gp.reset();
            }

            //CleanUp
            hitBox.translate((int) (-x), (int) (-y));
            gp.player.hitBox.translate((int) (-gp.player.x), (int) (-gp.player.y));
        }
    }

    public void stop() {
        started = false;
    }

    public void Panic() {

    }

    public void reset() {
        if (gp.currentScene instanceof PlayingScene scene) {
            switch (color) {
                case 'R' -> {
                    this.x = scene.GhostRedStart.x;
                    this.y = scene.GhostRedStart.y;
                }
                case 'G' -> {
                    this.x = scene.GhostGreenStart.x;
                    this.y = scene.GhostGreenStart.y;
                }
                case 'O' -> {
                    this.x = scene.GhostOrangeStart.x;
                    this.y = scene.GhostOrangeStart.y;
                }
                case 'B' -> {
                    this.x = scene.GhostBlueStart.x;
                    this.y = scene.GhostBlueStart.y;

                }
            }
        }
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

        g.drawImage(spriteSheet.getSprite((int) walkingAnimationTimer),(int) ((x  + GamePanel.Padding) * GamePanel.scale),(int) (y * GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);

        //Clones for the TP
        g.drawImage(spriteSheet.getSprite((int) walkingAnimationTimer), (int) ((x + GamePanel.Padding + GamePanel.LevelWidth) * GamePanel.scale), (int) (y* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);
        g.drawImage(spriteSheet.getSprite((int) walkingAnimationTimer), (int) ((x + GamePanel.Padding - GamePanel.LevelWidth) * GamePanel.scale), (int) (y* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) ((hitBox.x + x + GamePanel.Padding) * GamePanel.scale), (int) ((hitBox.y + y)* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));
        }
    }
}
