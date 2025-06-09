package Scenes;

import Entities.Coin;
import Entities.Ghost;
import Entities.Pacman;
import main.GamePanel;
import main.Path;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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

    private Font ScoreFont;

    public Path[] PathPoints;

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

        gp.ghosts = new Ghost[4];

        gp.ghosts[0] = new Ghost(gp,'R', GhostRedStart.x, GhostRedStart.y);
        gp.ghosts[1] = new Ghost(gp,'G', GhostGreenStart.x, GhostGreenStart.y);
        gp.ghosts[2] = new Ghost(gp,'O', GhostOrangeStart.x, GhostOrangeStart.y);
        gp.ghosts[3] = new Ghost(gp,'B', GhostBlueStart.x, GhostBlueStart.y);

        //Setup Coins
        try {
            String RawCoins = new String(getURL("/Level/Coins.txt").openStream().readAllBytes());
            String[] lines = RawCoins.split("\n");

            record Position(boolean big, int x, int y) {}

            ArrayList<Position> positions = new ArrayList<>();

            for (String line: lines) {
                String[] args = line.split(" ");
                if (args.length != 3) continue;

                positions.add(new Position("B".equals(args[0].trim()), Integer.parseInt(args[1].trim()),Integer.parseInt(args[2].trim())));
            }

            gp.coins = new Coin[positions.size()];

            for (int i = 0;i < gp.coins.length;i++) {
                Position position = positions.get(i);
                gp.coins[i] = new Coin(gp,position.big(), position.x(), position.y());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Setup PathPoints
        try {
            String RawCoins = new String(getURL("/Level/Paths.txt").openStream().readAllBytes());
            String[] lines = RawCoins.split("\n");

            ArrayList<Path> paths = new ArrayList<>();

            for (String line: lines) {
                String[] args = line.split(" ");
                if (args.length != 3) continue;

                paths.add(new Path(Integer.parseInt(args[1].trim()),Integer.parseInt(args[2].trim()),args[0].trim()));
            }

            PathPoints = paths.toArray(new Path[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            // Load the TTF file from resources or file system
            InputStream is = getURL("/Fonts/PressStart2P.ttf").openStream();
            ScoreFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) (8 * GamePanel.scale));
        } catch (Exception e) {
            e.printStackTrace();
            ScoreFont = new Font("SansSerif", Font.BOLD, (int) (8 * GamePanel.scale));
        }

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

    @Override
    public void reScale() {
        ScoreFont = ScoreFont.deriveFont((float) (8 * GamePanel.scale));
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

        g.drawImage(LevelImage, (int) (GamePanel.Padding * GamePanel.scale), 0, (int) (LevelImage.getWidth() * GamePanel.scale), (int) (LevelImage.getHeight() * GamePanel.scale), null);

        gp.player.draw(g);

        for (Ghost ghost: gp.ghosts) {
            ghost.draw(g);
        }

        for (Coin coin: gp.coins) {
            if (coin != null) coin.draw(g);
        }

        //Overlapping Characters
        g.setColor(gp.getBackground());
        g.fillRect(0,0, (int) (GamePanel.Padding * GamePanel.scale), GamePanel.screenheight);
        g.fillRect((int) ((GamePanel.LevelWidth + GamePanel.Padding) * GamePanel.scale),0, (int) (GamePanel.Padding * GamePanel.scale), GamePanel.screenheight);

        //UI
        g.setColor(Color.WHITE);
        g.setFont(ScoreFont);
        String text = String.valueOf(gp.player.Score);
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(text,g);

        //                                                 Padding                                         Padding
        g.drawString(text, (int) (GamePanel.screenwidth - 5 * GamePanel.scale - bounds.getWidth()),(int) (5 * GamePanel.scale + metrics.getAscent()));

        if (GamePanel.debug) {
            for (Path p : PathPoints) {
                g.setColor(Color.ORANGE);
                int x = (int) ((p.x() + GamePanel.Padding + 7) * GamePanel.scale);
                int y = (int) ((p.y() + 7) * GamePanel.scale);

                g.setColor(Color.GREEN);
                if (p.directions().contains("U")) g.drawLine(x, y, x, y - 10);
                if (p.directions().contains("L")) g.drawLine(x, y, x - 10, y);
                if (p.directions().contains("D")) g.drawLine(x, y, x, y + 10);
                if (p.directions().contains("R")) g.drawLine(x, y, x + 10, y);
            }
        }
    }
}
