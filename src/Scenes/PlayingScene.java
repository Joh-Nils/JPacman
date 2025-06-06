package Scenes;

import Entities.Coin;
import Entities.Ghost;
import Entities.Pacman;
import main.GamePanel;

import java.awt.*;

public class PlayingScene implements Scene{
    private GamePanel gp;

    public boolean paused = false;

    public Point PacmanStart;
    public Point GhostBlueStart;
    public Point GhostGreenStart;
    public Point GhostOrangeStart;
    public Point GhostRedStart;

    public int Level = 1;

    public PlayingScene(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void initialize() {
        gp.setBackground(new Color(12,3,26));


        PacmanStart = new Point(GamePanel.screenwidth/2,GamePanel.screenheight * 2 / 3);

        GhostBlueStart = new Point(GamePanel.screenwidth/2,GamePanel.screenheight / 3);
        GhostGreenStart = new Point(GamePanel.screenwidth/2,GamePanel.screenheight / 3);
        GhostOrangeStart = new Point(GamePanel.screenwidth/2,GamePanel.screenheight / 3);
        GhostRedStart = new Point(GamePanel.screenwidth/2,GamePanel.screenheight / 3);

        gp.player = new Pacman(gp);

        gp.ghosts[0] = new Ghost(gp,'R', 100, 100);
        gp.ghosts[1] = new Ghost(gp,'G', 164, 100);
        gp.ghosts[2] = new Ghost(gp,'O', 228, 100);
        gp.ghosts[3] = new Ghost(gp,'B', 292, 100);

        gp.coins = new Coin[2];

        gp.coins[0] = new Coin(gp,true, 100, 200);
        gp.coins[1] = new Coin(gp,false, 164, 200);
    }

    @Override
    public void reset() {
        paused = true;
        gp.player.stop();

        gp.ghosts[0].stop();
        gp.ghosts[1].stop();
        gp.ghosts[2].stop();
        gp.ghosts[3].stop();
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
        gp.tileManager.draw(g,400,0);

        gp.player.draw(g);

        for (Ghost ghost: gp.ghosts) {
            ghost.draw(g);
        }

        for (Coin coin: gp.coins) {
            if (coin != null) coin.draw(g);
        }
    }
}
