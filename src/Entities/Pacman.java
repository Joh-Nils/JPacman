package Entities;

import main.GamePanel;
import util.AssetPool;
import util.ImageTransform;
import util.SpriteSheet;

import java.awt.*;

import static util.getURL.getURL;

public class Pacman extends Entity {

    //Constants
    public final int speed;


    public int Score = 0;

    //Overlapping Memory with KeyHandler
    public boolean upPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean downPressed = false;

    public char direction = 'R'; //U - up; L - Left; D - Down; R - Right
    public char directionBuffer = 'R'; //U - up; L - Left; D - Down; R - Right
    public double bufferTimer = 0.2;


    public Pacman(GamePanel gp) {
        spriteSheet = AssetPool.getSpriteSheet(getURL("/images/PacMan.png"),16,16);
        spriteSheetLeft = ImageTransform.flipSpriteSheet(spriteSheet);
        spriteSheetUp = ImageTransform.rotateSpriteSheet(spriteSheet,-90);
        spriteSheetDown = ImageTransform.rotateSpriteSheet(spriteSheet,90);
        hitBox = new Rectangle(0,0,16,16);

        speed = 40 * GamePanel.scale;

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
        if (upPressed) directionBuffer = 'U';
        else if (leftPressed) directionBuffer = 'L';
        else if (downPressed) directionBuffer = 'D';
        else if (rightPressed) directionBuffer = 'R';

        if (directionBuffer != direction) {
            bufferTimer -= 1 * dt;

            if (checkValidInput()) {
                bufferTimer = 0.2;

                direction = directionBuffer;
            }

            else if (bufferTimer <= 0.0) {
                bufferTimer = 0.2;

                directionBuffer = direction;
            }
        }

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
    private boolean checkValidInput() {
        return true; //TODO
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
