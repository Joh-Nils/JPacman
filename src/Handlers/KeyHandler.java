package Handlers;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) gp.player.upPressed = true;
        else if (e.getKeyCode() == KeyEvent.VK_A) gp.player.leftPressed = true;
        else if (e.getKeyCode() == KeyEvent.VK_S) gp.player.downPressed = true;
        else if (e.getKeyCode() == KeyEvent.VK_D) gp.player.rightPressed = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) gp.player.upPressed = false;
        else if (e.getKeyCode() == KeyEvent.VK_A) gp.player.leftPressed = false;
        else if (e.getKeyCode() == KeyEvent.VK_S) gp.player.downPressed = false;
        else if (e.getKeyCode() == KeyEvent.VK_D) gp.player.rightPressed = false;

    }
}
