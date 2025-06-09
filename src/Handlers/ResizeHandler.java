package Handlers;

import main.GamePanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeHandler extends ComponentAdapter {
    private GamePanel gp;
    private Dimension lastSize;

    public ResizeHandler(GamePanel gp) {
        this.gp = gp;
        lastSize = new Dimension(GamePanel.screenwidth, GamePanel.screenheight);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (gp.gf == null) return;

        Dimension newSize = gp.gf.getSize();
        if (!newSize.equals(lastSize)) {

            if (lastSize.getHeight() != newSize.getHeight()) {
                GamePanel.screenwidth = (int) (newSize.getHeight() * GamePanel.ASPECT_RATIO);
                GamePanel.screenheight = (int) newSize.getHeight();

                gp.setPreferredSize(new Dimension(GamePanel.screenwidth, GamePanel.screenheight));

                gp.gf.pack();

                lastSize.setSize(gp.gf.getSize());

                GamePanel.scale = (double) GamePanel.screenheight / GamePanel.Height;

                gp.currentScene.reScale();
            }
            else if (lastSize.getWidth() != newSize.getWidth()) {
                GamePanel.screenwidth = (int) newSize.getWidth();
                GamePanel.screenheight = (int) (newSize.getWidth() / GamePanel.ASPECT_RATIO);


                gp.setPreferredSize(new Dimension(GamePanel.screenwidth, GamePanel.screenheight));

                gp.gf.pack();

                lastSize.setSize(gp.gf.getSize());

                GamePanel.scale = (double) GamePanel.screenwidth / GamePanel.Width;

                gp.currentScene.reScale();
            }
        }
    }
}
