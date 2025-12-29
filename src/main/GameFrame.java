package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static util.getURL.getURL;

public class GameFrame extends JFrame {
    GamePanel gp;

    public GameFrame(GamePanel gp) throws IOException {
        Toolkit.getDefaultToolkit().setDynamicLayout(false);

        setLayout(null);
        setTitle("JPacman --made by JohNils");
        requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);


        gp.setPreferredSize(new Dimension(GamePanel.screenwidth, GamePanel.screenheight));
        gp.setVisible(true);

        setContentPane(gp);
        pack();

        setMinimumSize(new Dimension(GamePanel.Width, GamePanel.Height));

        setLocationRelativeTo(null);

        addKeyListener(gp.kHandler);
        addWindowListener(gp.wHandler);
        addComponentListener(gp.rHandler);

        this.gp = gp;

        setIcon();

        setVisible(true);
    }

    public void setIcon() throws IOException {
        BufferedImage icon = ImageIO.read(getURL("/images/PacMan.png"));
        setIconImage(icon.getSubimage(64,0,16,16));
    }
}
