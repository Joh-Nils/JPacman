package Entities;

import Scenes.DeathScene;
import Scenes.PlayingScene;
import main.GamePanel;
import main.Path;
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

    public char direction = 'R'; //U - up; L - Left; D - Down; R - Right; Z - Death
    public char directionBuffer = ' '; //U - up; L - Left; D - Down; R - Right
    public double bufferTimer = 0.2;

    public boolean started;

    private SpriteSheet Death;

    public Pacman(GamePanel gp, Point Start) {
        this.gp = gp;

        spriteSheet = AssetPool.getSpriteSheet(getURL("/images/PacMan.png"),16,16);
        spriteSheetLeft = ImageTransform.flipSpriteSheet(spriteSheet);
        spriteSheetUp = ImageTransform.rotateSpriteSheet(spriteSheet,-90);
        spriteSheetDown = ImageTransform.rotateSpriteSheet(spriteSheet,90);
        Death = AssetPool.getSpriteSheet(getURL("/images/Death.png"),16,16);

        hitBox = new Rectangle(0,0,16,16);

        speed = 40;

        x = Start.x;
        y = Start.y;
    }

    @Override
    public void update(float dt) {
        //Started?
        if (!started) {
            if (direction == 'Z') {
                walkingAnimationTimer += (float) 5 * dt;
                if (walkingAnimationTimer > spriteSheet.getSprites().length) {
                    //Hide reset Level
                    if (gp.currentScene instanceof PlayingScene scene) scene.resetLevel();
                    direction = 'R';
                    walkingAnimationTimer = 0;
                }
                return;
            }

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

        if (direction != ' ') {
            walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
            if (walkingAnimationTimer >= spriteSheet.getSprites().length)
                walkingAnimationTimer = walkingAnimationTimer % spriteSheet.getSprites().length;
        }

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
        started = false;
        directionBuffer = ' ';
    }

    public void die() {
        walkingAnimationTimer = 0;
        direction = 'Z';
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

            case ' ' -> {}

            default -> {
                assert false : "Unknown Direction Key: '" + direction + "'";
            }
        }

        //Hideous and imperformant solution
        if (gp.currentScene instanceof PlayingScene playingScene) {
            for (Path path : playingScene.PathPoints) {
                double distanceX = path.x() - this.x;
                double distanceY = path.y() - this.y;
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance < 1) { //Not a great solution but if it works, it works
                    if (!path.directions().contains(String.valueOf(direction))) {
                        //Stop
                        direction = ' ';
                        x = path.x();
                        y = path.y();
                    }
                }
            }
        }

        if (x > GamePanel.LevelWidth) {
            x = x % GamePanel.LevelWidth;
        }
        else if (x + hitBox.width < 0) {
            x = ((x % GamePanel.LevelWidth) + GamePanel.LevelWidth);
        }
    }
    private boolean checkValidInput() {
        if (directionBuffer == ' ') return false;
        if (!started && (directionBuffer == 'R' || directionBuffer == 'L')) return true;

        if (gp.currentScene instanceof PlayingScene playingScene) {
            if (isOpposite(direction, directionBuffer)) {
                return true;
            }

            for (Path path : playingScene.PathPoints) {
                double distanceX = path.x() - this.x;
                double distanceY = path.y() - this.y;
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance < 1) { //Not a great solution but if it works, it works
                    if (path.directions().contains(String.valueOf(directionBuffer))) {
                        x = path.x(); //Correct alignment
                        y = path.y();
                        return true;
                    }
                }
            }
            return false;
        }

        return true;
    }
    private boolean isOpposite(char a, char b) {
        return (a == 'U' && b == 'D') ||
                (a == 'D' && b == 'U') ||
                (a == 'L' && b == 'R') ||
                (a == 'R' && b == 'L');
    }

    SpriteSheet drawSpriteSheet = null;
    @Override
    public void draw(Graphics2D g) {
        if (walkingAnimationTimer < spriteSheet.getSprites().length) {

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
                case 'Z' -> {
                    drawSpriteSheet = Death;
                }
                case ' ' -> {}

                default -> {
                    assert false : "Unknown Direction Key: '" + direction + "'";
                }
            }
            g.drawImage(drawSpriteSheet.getSprite((int) walkingAnimationTimer), (int) ((x + GamePanel.Padding) * GamePanel.scale), (int) (y* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);

            //Clones for the TP
            g.drawImage(drawSpriteSheet.getSprite((int) walkingAnimationTimer), (int) ((x + GamePanel.Padding + GamePanel.LevelWidth) * GamePanel.scale), (int) (y* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);
            g.drawImage(drawSpriteSheet.getSprite((int) walkingAnimationTimer), (int) ((x + GamePanel.Padding - GamePanel.LevelWidth) * GamePanel.scale), (int) (y* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale), null);
        }

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) ((hitBox.x + x + GamePanel.Padding) * GamePanel.scale), (int) ((hitBox.y + y) * GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));
        }
    }
}
