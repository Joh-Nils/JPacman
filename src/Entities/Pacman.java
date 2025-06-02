package Entities;

import main.GamePanel;
import util.AssetPool;
import util.ImageTransform;
import util.SpriteSheet;

import java.awt.*;

import static util.getURL.getURL;

public class Pacman extends Entity {

    //Constants
    public static final int speed = 150;


    public int Score = 0;

    //Overlapping Memory with KeyHandler
    public boolean upPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean downPressed = false;

    public char direction = 'R'; //U - up; L - Left; D - Down; R - Right


    public Pacman() {
        spriteSheet = AssetPool.getSpriteSheet(getURL("/images/PacMan.png"),16,16);
        spriteSheetLeft = ImageTransform.flipSpriteSheet(spriteSheet);
        spriteSheetUp = ImageTransform.rotateSpriteSheet(spriteSheet,-90);
        spriteSheetDown = ImageTransform.rotateSpriteSheet(spriteSheet,90);
        hitBox = new Rectangle(0,0,16,16);

        x = 100;
        y = 100;
    }

    @Override
    public void update(float dt) {
        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer > spriteSheet.getSprites().length) walkingAnimationTimer -= spriteSheet.getSprites().length;

        move(dt);
    }

    private void move(float dt) {
        if (upPressed) direction = 'U';
        else if (leftPressed) direction = 'L';
        else if (downPressed) direction = 'D';
        else if (rightPressed) direction = 'R';

        //TODO Collision Handling
        switch (direction) {
            case 'U' -> {
                y -= (double) speed * dt;
            }
            case 'L' -> {
                x -= (double) speed * dt;
            }
            case 'D' -> {
                y += (double) speed * dt;
            }
            case 'R' -> {
                x += (double) speed * dt;
            }

            default -> {
                assert false : "Unknown Direction Key: '" + direction + "'";
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        SpriteSheet drawSpriteSheet = null;

        switch (direction) {
            case 'U' -> {
                drawSpriteSheet = spriteSheetUp;
            }
            case 'L' -> {
                drawSpriteSheet = spriteSheetLeft;
            }
            case 'D' -> {
                drawSpriteSheet = spriteSheetDown;
            }
            case 'R' -> {
                drawSpriteSheet = spriteSheet;
            }

            default -> {
                assert false : "Unknown Direction Key: '" + direction + "'";
            }
        }
        g.drawImage(drawSpriteSheet.getSprite((int) walkingAnimationTimer),(int) x,(int) y,hitBox.width * GamePanel.scale, hitBox.height * GamePanel.scale, null);


        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) (hitBox.x * GamePanel.scale + x), (int) (hitBox.y * GamePanel.scale + y), hitBox.width * GamePanel.scale, hitBox.height * GamePanel.scale);
        }
    }
}
