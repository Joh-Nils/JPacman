package Entities;

import Scenes.PlayingScene;
import main.GamePanel;
import main.Path;
import util.AssetPool;
import util.SpriteSheet;

import java.awt.*;
import java.util.Random;

import static util.getURL.getURL;

public class Ghost extends Entity{
    private static final Random random = new Random();
    private static SpriteSheet eyeMask;

    private GamePanel gp;

    public boolean started = false;

    public char direction = 'L';

    public char color = ' ';

    private double speed;

    private boolean moveOut = true;

    private boolean returnHome = false;

    private double scatterSpeed;
    private double frightenedSpeed;

    private Point lastMovePoint = new Point(0,0);

    private double frightened = 0.0;
    private double frightenedHighlight = 0.0;
    private boolean frightenedHighlightAdd = true;
    private SpriteSheet frightenedSpriteSheet;
    private SpriteSheet frightenedHighlightSpriteSheet;

    private static boolean scatter = true;
    private boolean ClydeScatter = false;
    private static double scatterTimer = 0.0;

    private double Out = 0.0;

    private Path lastRandomPath = null;

    private boolean moveUp = false;

    private static final double ClydeDistance = 8 * 16.0;

    public Ghost(GamePanel gp, char color, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.color = color;

        if (eyeMask == null) eyeMask = AssetPool.getSpriteSheet(getURL("/images/eyeMask.png"), 16, 16);

        switch (color) {
            case 'R' -> {
                spriteSheet = AssetPool.getSpriteSheet(getURL("/images/redGhost.png"),16,16);
                speed = gp.player.speed;
            }
            case 'G' -> {
                spriteSheet = AssetPool.getSpriteSheet(getURL("/images/greenGhost.png"),16,16);
                speed = 0.9 * gp.player.speed;
            }
            case 'O' -> {
                spriteSheet = AssetPool.getSpriteSheet(getURL("/images/orangeGhost.png"),16,16);
                speed = 0.7 * gp.player.speed;
            }
            case 'B' -> {
                spriteSheet = AssetPool.getSpriteSheet(getURL("/images/blueGhost.png"),16,16);
                speed = 0.85 * gp.player.speed;
            }
        }

        spriteSheet.registerMask(eyeMask);

        frightenedSpriteSheet = AssetPool.getSpriteSheet(getURL("/images/frightenedGhost.png"),16,16);
        frightenedHighlightSpriteSheet = AssetPool.getSpriteSheet(getURL("/images/frightenedHighlightGhost.png"),16,16);

        scatterSpeed = 0.9 * gp.player.speed;
        frightenedSpeed = 0.65 * gp.player.speed;

        hitBox = new Rectangle(4,4,8,8);
    }

    private int targetX = -100;
    private int targetY = -100;

    @Override
    public void update(float dt) {
        if (frightened > 0) {
            frightened -= dt;

            if (frightened <= 2) {
                if (frightenedHighlightAdd) {
                    frightenedHighlight += 6 * dt;

                    if (frightenedHighlight >= 2) {
                        frightenedHighlight = 2;
                        frightenedHighlightAdd = false;
                    }
                }
                else {
                    frightenedHighlight -= 6 * dt;

                    if (frightenedHighlight <= 0) {
                        frightenedHighlight = 0;
                        frightenedHighlightAdd = true;
                    }

                }
            }

        }

        if (!started) {
            if (!gp.player.started) return;

            if (moveUp) {
                y -= speed * dt;

                if (y <= 125) {
                    moveUp = false;
                    y = 125;
                }
            }
            else {
                y += speed * dt;

                if (y + 16 >= 148) {
                    moveUp = true;
                    y = 148 - 16;
                }

            }

            switch (color) {
                case 'G' -> {
                    if (!gp.ghosts[0].started) return;
                    Out += dt;
                    if (Out > 4.0) start();
                }
                case 'B' -> {
                    Out += dt;
                    if (gp.player.pelletsEaten > 30 && Out > 8.0) start();
                }
                case 'O' -> {
                    Out += dt;
                    if (gp.player.pelletsEaten > 60 && Out > 12.0) start();
                }
            }

            return;
        }

        walkingAnimationTimer += (float) GamePanel.animationSpeeds * dt;
        if (walkingAnimationTimer >= spriteSheet.getSprites().length) walkingAnimationTimer = walkingAnimationTimer % spriteSheet.getSprites().length;

        if (moveOut) {

            if (x != 121.0) {
                x -= (speed * dt) * (x > 121.0 ? 1 : -1);

                if (Math.floor(x) == 121) {
                    x = 121;
                }
                return;
            }

            y -= speed * dt;

            if (Math.floor(y) <= 101) {
                y = 101;
                moveOut = false;
            }

            return;
        }


        char newDirection = direction;
        if (returnHome) {
            if (gp.currentScene instanceof PlayingScene scene) {
                if ((int) x == scene.GhostRedStart.x && (int) y == scene.GhostRedStart.y) {
                    returnHome = false;
                }
                else {
                    newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostRedStart.x, scene.GhostRedStart.y, flip(direction));
                }
            }
        }
        else if (frightened > 0) {
            if (gp.currentScene instanceof PlayingScene playingScene) {
                for (Path path : playingScene.PathPoints) {
                    if (path == lastRandomPath) continue;

                    if (path.x() == x && Math.abs(path.y() - y) < 1.0 ||
                            path.y() == y && Math.abs(path.x() - x) < 1.0) {
                        if (path.directions().length() - 1 <= 0) break;
                        newDirection = path.directions().replace(flip(direction) + "","").charAt(random.nextInt(path.directions().length() - 1));
                        lastRandomPath = path;
                        break;
                    }
                }
            }
        }
        else if (scatter) {
            if (gp.currentScene instanceof PlayingScene scene) {
                switch (color) {
                    case 'R' -> {
                        newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostRedScatterPoint.x, scene.GhostRedScatterPoint.y, flip(direction));
                    }
                    case 'G' -> {
                        newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostGreenScatterPoint.x, scene.GhostGreenScatterPoint.y, flip(direction));
                    }
                    case 'O' -> {
                        ClydeScatter = false;
                        newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostOrangeScatterPoint.x, scene.GhostOrangeScatterPoint.y, flip(direction));
                    }
                    case 'B' -> {
                        newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostBlueScatterPoint.x, scene.GhostBlueScatterPoint.y, flip(direction));

                    }
                }
            }
        }
        else if (ClydeScatter) {
            if (gp.currentScene instanceof PlayingScene scene) {
                if (distance(x, y, scene.GhostOrangeScatterPoint.x, scene.GhostOrangeScatterPoint.y) < 1.0) ClydeScatter = false;
                newDirection = gp.astar.getDirection((int) x, (int) y, scene.GhostOrangeScatterPoint.x, scene.GhostOrangeScatterPoint.y, flip(direction));
            }
        }
        else {
            switch (color) {
                case 'G' -> {
                    Point target = gp.player.getAheadPosition(4, true);
                    targetX = target.x;
                    targetY = target.y;
                    newDirection = gp.astar.getDirection((int) x, (int) y, (int) target.x, (int) target.y, flip(direction));
                    if (newDirection == ' ') {

                        double best = Double.POSITIVE_INFINITY;
                        boolean better = false;
                        Path bestPath = new Path(0,0,"");
                        if (gp.currentScene instanceof PlayingScene playingScene) {
                            for (Path path : playingScene.PathPoints) {
                                if (path.x() == target.x && Math.abs(path.y() - y) < best) {
                                    best = Math.abs(path.y() - y);
                                    bestPath = path;
                                    better = true;
                                }
                                if (path.y() == target.y && Math.abs(path.x() - x) < best) {
                                    best = Math.abs(path.x() - x);
                                    bestPath = path;
                                    better = true;
                                }

                                double d = distance(target.x,target.y,path.x(),path.y());
                                if (!better && d < best) {
                                    best = d;
                                    bestPath = path;
                                }
                            }
                        }

                        target = new Point(bestPath.x(), bestPath.y());
                        newDirection = gp.astar.getDirection((int) x, (int) y, (int) target.x, (int) target.y, flip(direction));
                    }
                }
                case 'B' -> {
                    Point help = gp.player.getAheadPosition(2, false);

                    Point target = new Point((int) (help.x + (help.x - gp.ghosts[1].x)), (int) (help.y + (help.y - gp.ghosts[1].y))); //Take Vector between help and Pinky and double it


                    targetX = target.x;
                    targetY = target.y;
                    newDirection = gp.astar.getDirection((int) x, (int) y, target.x, target.y, flip(direction));
                    if (newDirection == ' ') {

                        double best = Double.POSITIVE_INFINITY;
                        boolean better = false;
                        Path bestPath = new Path(0,0,"");
                        if (gp.currentScene instanceof PlayingScene playingScene) {
                            for (Path path : playingScene.PathPoints) {
                                if (path.x() == target.x && Math.abs(path.y() - y) < best) {
                                    best = Math.abs(path.y() - y);
                                    bestPath = path;
                                    better = true;
                                }
                                if (path.y() == target.y && Math.abs(path.x() - x) < best) {
                                    best = Math.abs(path.x() - x);
                                    bestPath = path;
                                    better = true;
                                }

                                double d = distance(target.x,target.y,path.x(),path.y());
                                if (!better && d < best) {
                                    best = d;
                                    bestPath = path;
                                }
                            }
                        }

                        target = new Point(bestPath.x(), bestPath.y());
                        newDirection = gp.astar.getDirection((int) x, (int) y, (int) target.x, (int) target.y, flip(direction));
                    }
                }
                case 'O' -> {
                    if (distance(x, y, gp.player.x, gp.player.y) < ClydeDistance) ClydeScatter = true;

                    newDirection = gp.astar.getDirection((int) x, (int) y, (int) gp.player.x, (int) gp.player.y, flip(direction));
                }

                default -> newDirection = gp.astar.getDirection((int) x, (int) y, (int) gp.player.x, (int) gp.player.y, flip(direction)); //Blinky
            }
        }

        if (newDirection != direction && flip(newDirection) != direction &&
                !(lastMovePoint.x == (int) x && lastMovePoint.y == (int) y) && newDirection != ' ') {
            direction = newDirection;

            if (gp.currentScene instanceof PlayingScene playingScene) {
                for (Path path : playingScene.PathPoints) {
                    if (path.x() == x && Math.abs(path.y() - y) < 1.0) {
                        y = path.y();
                    }
                    if (path.y() == y && Math.abs(path.x() - x) < 1.0) {
                        x = path.x();
                    }
                }

                lastMovePoint.x = (int) x;
                lastMovePoint.y = (int) y;
            }
        }

        if (direction != ' ') move(dt);

        if (x > GamePanel.LevelWidth) {
            x = x % GamePanel.LevelWidth;
        }
        else if (x + hitBox.width < 0) {
            x = ((x % GamePanel.LevelWidth) + GamePanel.LevelWidth);
        }
        if (y + 17 > GamePanel.LevelHeight) {
            y = GamePanel.LevelHeight - 17;
        }
        else if (y < 1) {
            y = 1;
        }

        if (!returnHome) {
            hitBox.translate((int) (x), (int) (y));
            gp.player.hitBox.translate((int) (gp.player.x), (int) (gp.player.y));

            if (hitBox.intersects(gp.player.hitBox)) {
                if (frightened > 0.0) {
                    gp.player.Score += gp.player.ghostMultiplier;

                    if (gp.player.ghostMultiplier == 200) gp.player.ghostMultiplier = 400;
                    else if (gp.player.ghostMultiplier == 400) gp.player.ghostMultiplier = 800;
                    else if (gp.player.ghostMultiplier == 800) gp.player.ghostMultiplier = 1600;

                    returnHome = true;
                    frightened = 0.0;
                } else {
                    gp.player.Lives--;
                    gp.reset();
                }
            }

            //CleanUp
            hitBox.translate((int) (-x), (int) (-y));
            gp.player.hitBox.translate((int) (-gp.player.x), (int) (-gp.player.y));
        }
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static void updateScatterTimer(float dt) {
        scatterTimer += dt;

        if (scatterTimer > 59.0) scatter = false;
        else if (scatterTimer > 54.0) scatter = true;
        else if (scatterTimer > 34.0) scatter = false;
        else if (scatterTimer > 27.0) scatter = true;
        else if (scatterTimer > 7.0) scatter = false;
    }

    private char flip(char i) {
        return i == 'U' ? 'D' :
                i == 'D' ? 'U' :
                i == 'L' ? 'R' : 'L';
    }

    public void stop() {
        started = false;
    }
    public void start() {
        started = true;
    }

    public void Panic() {
        if (returnHome || !started) return;
        frightened = 6;
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

        moveOut = true;
        Out = 0;
        direction = 'L';
        returnHome = false;
    }

    private void move(float dt) {
        double currentSpeed = speed;

        if (returnHome) currentSpeed = 2 * speed;
        else if (frightened > 0) currentSpeed = frightenedSpeed;
        else if (scatter) currentSpeed = scatterSpeed;

        switch (direction) {
            case 'U' -> {
                y -= currentSpeed * dt;
            }
            case 'L' -> {
                x -=  currentSpeed * dt;
            }
            case 'D' -> {
                y += currentSpeed * dt;
            }
            case 'R' -> {
                x += currentSpeed * dt;
            }

            default -> {
                assert false : "Unknown Direction Key: '" + direction + "'";
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {

        int drawX = (int) ((x + GamePanel.Padding) * GamePanel.scale);
        int drawY = (int) (y * GamePanel.scale);

        int cloneLeftDrawX = (int) ((x + GamePanel.Padding - GamePanel.LevelWidth) * GamePanel.scale);
        int cloneRightDrawX = (int) ((x + GamePanel.Padding + GamePanel.LevelWidth) * GamePanel.scale);

        if (frightened > 0) {
            if (frightenedHighlight < 1.0) {
                draw(g, frightenedSpriteSheet, drawX, drawY, cloneLeftDrawX, cloneRightDrawX);
            }
            else {
                draw(g, frightenedHighlightSpriteSheet, drawX, drawY, cloneLeftDrawX, cloneRightDrawX);
            }
        }
        else if (returnHome) {
            draw(g, spriteSheet.getMasked(eyeMask), drawX, drawY, cloneLeftDrawX, cloneRightDrawX);
        }
        else {
            draw(g, spriteSheet, drawX, drawY, cloneLeftDrawX, cloneRightDrawX);
        }

        if (GamePanel.debug) {
            g.setColor(Color.WHITE);
            g.drawRect((int) ((hitBox.x + x + GamePanel.Padding) * GamePanel.scale), (int) ((hitBox.y + y)* GamePanel.scale), (int) (hitBox.width * GamePanel.scale), (int) (hitBox.height * GamePanel.scale));

            g.drawString(String.valueOf(direction), (int) ((x +8+ GamePanel.Padding) * GamePanel.scale), (int) ((y)* GamePanel.scale) - 16);

            if (color == 'G') {
                g.setColor(Color.GREEN);

                g.drawRect((int) ((targetX + GamePanel.Padding) * GamePanel.scale), (int) ((targetY)* GamePanel.scale), (int) (16 * GamePanel.scale), (int) (16 * GamePanel.scale));
            }
            if (color == 'B') {
                g.setColor(Color.BLUE);

                g.drawRect((int) ((targetX + GamePanel.Padding) * GamePanel.scale), (int) ((targetY)* GamePanel.scale), (int) (16 * GamePanel.scale), (int) (16 * GamePanel.scale));
            }
        }
    }

    private void draw(Graphics2D g, SpriteSheet spriteSheet, int x,int y, int cloneLeftX, int cloneRightX) {
        drawSpriteSheet(g, spriteSheet,(int) walkingAnimationTimer, x, y);

        //Clones for the TP
        drawSpriteSheet(g, spriteSheet,(int) walkingAnimationTimer, cloneLeftX, y);
        drawSpriteSheet(g, spriteSheet,(int) walkingAnimationTimer, cloneRightX, y);}
}
