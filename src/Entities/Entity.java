package Entities;

import main.GamePanel;
import util.SpriteSheet;

import java.awt.*;

public abstract class Entity {

    protected double x;
    protected double y;

    protected Rectangle hitBox;

    protected SpriteSheet spriteSheet;
    protected SpriteSheet spriteSheetLeft;
    protected SpriteSheet spriteSheetUp;
    protected SpriteSheet spriteSheetDown;

    protected float walkingAnimationTimer = 0.0F;




    public abstract void update(float dt);
    public abstract void draw(Graphics2D g);

    protected final void drawSpriteSheet(Graphics2D g, SpriteSheet spriteSheet, int timer, int x, int y) {
        g.drawImage(spriteSheet.getSprite(timer), x, y, (int) (spriteSheet.getSpriteWidth() * GamePanel.scale), (int) (spriteSheet.getSpriteHeight() * GamePanel.scale), null);
    }
}
