package Scenes;

import main.GamePanel;

import java.awt.*;

public class GameOverScene implements Scene {
    private GamePanel gp;
    private PlayingScene playingScene;

    public GameOverScene(GamePanel gp, PlayingScene playingScene) {
        this.gp = gp;
        this.playingScene = playingScene;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void reScale() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(Graphics2D g) {
        playingScene.drawWithoutUI(g);

        g.setColor(Color.BLACK);
        g.setComposite(AlphaComposite.SrcOver.derive(0.4F));
        g.fillRect(0,0,GamePanel.screenwidth,GamePanel.screenheight);
        g.setComposite(AlphaComposite.SrcOver.derive(1F));

        g.setFont(playingScene.ScoreFont.deriveFont(Font.PLAIN,(float) (33 * GamePanel.scale)));

        int x = (int) ((double) GamePanel.screenwidth /2 - g.getFontMetrics().getStringBounds("Game Over",g).getWidth()/2);
        int y = (int) (GamePanel.screenheight/3);

        g.setColor(Color.RED);
        g.drawString("Game Over", (int) (x + 2 * GamePanel.scale), (int) (y + 2 * GamePanel.scale));

        g.setFont(playingScene.ScoreFont.deriveFont(Font.PLAIN, (float) (13 * GamePanel.scale)));
        x = (int) ((double) GamePanel.screenwidth /2 - g.getFontMetrics().getStringBounds("Score: " + gp.player.Score,g).getWidth()/2);
        y = (int) (GamePanel.screenheight/5*3);
        g.setColor(Color.ORANGE);
        g.drawString("Score: " + gp.player.Score, x, y);
    }
}
