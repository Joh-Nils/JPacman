package Handlers;

import Scenes.TitleScene;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private GamePanel gp;

    public boolean somethingPressed = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gp.player != null) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) gp.player.upPressed = true;
            else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
                gp.player.leftPressed = true;
            else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
                gp.player.downPressed = true;
            else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                gp.player.rightPressed = true;
        }

        somethingPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (gp.player != null) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) gp.player.upPressed = false;
            else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
                gp.player.leftPressed = false;
            else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
                gp.player.downPressed = false;
            else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                gp.player.rightPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            gp.changeScene(TitleScene.class);
        }

        somethingPressed = false;
    }
}
