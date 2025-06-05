package Scenes;

import main.GamePanel;

import java.awt.*;

public class TitleScene implements Scene {
    private GamePanel gp;

    public TitleScene(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void initialize() {
        gp.setBackground(new Color(6,1,13));

    }

    @Override
    public void reset() {

    }

    int timer = 144 * 4;
    @Override
    public void update(float dt) {
        timer--;
        if (timer <= 0) {
            gp.changeScene(new PlayingScene(gp));
        }
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
