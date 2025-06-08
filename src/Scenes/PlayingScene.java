package Scenes;

import Entities.Coin;
import Entities.Ghost;
import Entities.Pacman;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static util.getURL.getURL;

public class PlayingScene implements Scene{
    private GamePanel gp;

    public boolean paused = false;

    public Point PacmanStart;
    public Point GhostBlueStart;
    public Point GhostGreenStart;
    public Point GhostOrangeStart;
    public Point GhostRedStart;

    public int Level = 1;

    public BufferedImage LevelImage;

    public PlayingScene(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void initialize() {
        gp.setBackground(new Color(12,3,26));

        PacmanStart = new Point(121,221);

        GhostBlueStart = new Point(137,132);
        GhostGreenStart = new Point(106,132);
        GhostOrangeStart = new Point(121,132);
        GhostRedStart = new Point(121,117);

        gp.player = new Pacman(gp, PacmanStart);

        gp.ghosts[0] = new Ghost(gp,'R', GhostRedStart.x, GhostRedStart.y);
        gp.ghosts[1] = new Ghost(gp,'G', GhostGreenStart.x, GhostGreenStart.y);
        gp.ghosts[2] = new Ghost(gp,'O', GhostOrangeStart.x, GhostOrangeStart.y);
        gp.ghosts[3] = new Ghost(gp,'B', GhostBlueStart.x, GhostBlueStart.y);

        gp.coins = new Coin[2];

        gp.coins[0] = new Coin(gp,true, 100, 200);
        gp.coins[1] = new Coin(gp,false, 164, 200);

        try {
            LevelImage = ImageIO.read(getURL("/Level/Level.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        paused = true;
        gp.player.stop();
        gp.player.die();

        gp.ghosts[0].stop();
        gp.ghosts[1].stop();
        gp.ghosts[2].stop();
        gp.ghosts[3].stop();
    }

    public void resetLevel() {
        paused = false;
        gp.player.reset();

        gp.ghosts[0].reset();
        gp.ghosts[1].reset();
        gp.ghosts[2].reset();
        gp.ghosts[3].reset();
    }

    @Override
    public void update(float dt) {
        gp.player.update(dt);

        for (Ghost ghost: gp.ghosts) {
            ghost.update(dt);
        }

        if (gp.player.started) {
            for (Coin coin : gp.coins) {
                if (coin != null) coin.update(dt);
            }
        }

    }

    @Override
    public void draw(Graphics2D g) {
        //gp.tileManager.draw(g,400,0);

        g.drawImage(LevelImage, 0, 0, (int) (LevelImage.getWidth() * GamePanel.scale), (int) (LevelImage.getHeight() * GamePanel.scale), null);

        gp.player.draw(g);

        for (Ghost ghost: gp.ghosts) {
            ghost.draw(g);
        }

        for (Coin coin: gp.coins) {
            if (coin != null) coin.draw(g);
        }
    }
}
