package Entities;

import Scenes.DeathScene;
import Scenes.PlayingScene;
import main.GamePanel;
import util.AssetPool;
import util.ImageTransform;
import util.SpriteSheet;

import java.awt.*;

import static util.getURL.getURL;

public class Pacman extends Entity {
    private GamePanel gp;

    //Constants
    public final double speed;

    public int Score = 0;
    public int Lives = 3;

    //Overlapping Memory with KeyHandler
    public boolean upPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean downPressed = false;

    public char direction = 'R'; //U - up; L - Left; D - Down; R - Right
    public char directionBuffer = ' '; //U - up; L - Left; D - Down; R - Right
    public double bufferTimer = 0.2;

    public boolean started;

    public Pacman(GamePanel gp) {
        this.gp = gp;

        spriteSheet = AssetPool.getSpriteSheet(getURL("/images/PacMan.png"),16,16);
        spriteSheetLeft = ImageTransform.flipSpriteSheet(spriteSheet);
        spriteSheetUp = ImageTransform.rotateSpriteSheet(spriteSheet,-90);
        spriteSheetDown = ImageTransform.rotateSpriteSheet(spriteSheet,90);
        hitBox = new Rectangle(0,0,16,16);

        speed = 40 * GamePanel.scale;

        if (gp.currentScene != null &&
                gp.currentScene.getClass().equals(PlayingScene.class)) {
            PlayingScene scene = (PlayingScene) gp.currentScene;
            x = scene.PacmanStart.x;
            y = scene.PacmanStart.y;
        }
    }

    @Override
    public void update(float dt) {
        //Started?
        if (!started) {
            if (upPressed) directionBuffer = 'U';
            else if (leftPressed) directionBuffer = 'L';
            else if (downPressed) directionBuffer = 'D';
            else if (rightPressed) directionBuffer = 'R';

            if (checkValidInput()) {
                direction = directionBuffer;

                started = true;
                gp.ghosts[0].started = true;
                gp.ghosts[1].started = true;
                gp.ghosts[2].started = true;
                gp.ghosts[3].started = true;
            }

            return;
        }

        //Check Lives
        if (Lives <= 0) {
            if (!gp.currentScene.getClass().equals(DeathScene.class)) {
                gp.changeScene(new DeathScene(gp));
            }

            return; //Stop Updating while dead
        }

        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer > spriteSheet.getSprites().length) walkingAnimationTimer -= spriteSheet.getSprites().length;

        move(dt);

    }

    public void reset() {
        if (gp.currentScene.getClass().equals(PlayingScene.class)) {
            PlayingScene scene = (PlayingScene) gp.currentScene;
            x = scene.PacmanStart.x;
            y = scene.PacmanStart.y;
        }


        gp.player.started = false;
        gp.player.directionBuffer = ' ';
    }

    public void stop() {
        gp.player.started = false;
        gp.player.directionBuffer = ' ';
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
    private boolean checkValidInput() {
        if (directionBuffer == ' ') return false;

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
        g.drawImage(drawSpriteSheet.getSprite((int) walkingAnimationTimer),(int) x,(int) y, (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);


        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) (hitBox.x * GamePanel.scale + x), (int) (hitBox.y * GamePanel.scale + y), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));
        }
    }
}
