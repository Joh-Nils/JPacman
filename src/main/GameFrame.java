package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GamePanel gp;

    public GameFrame(GamePanel gp) {
        Toolkit.getDefaultToolkit().setDynamicLayout(false);

        setLayout(null);
        setTitle("Kindomfight");
        requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        //addKeyListener(new KeyHandler());
        //addMouseListener(new MouseHandler());


        gp.setPreferredSize(new Dimension(GamePanel.screenwidth, GamePanel.screenheight));
        gp.setVisible(true);

        setContentPane(gp);
        pack();

        setMinimumSize(new Dimension(GamePanel.Width, GamePanel.Height));

        setLocationRelativeTo(null);

        addKeyListener(gp.kHandler);

        //addMouseListener(gp.mouseHandler);
        //addMouseMotionListener(gp.mouseHandler);
        //addMouseWheelListener(gp.mouseHandler);

        addWindowListener(gp.wHandler);
        addComponentListener(gp.rHandler);

        this.gp = gp;

        //setIcon();


        setVisible(true);
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Bilder/ICO.png"));
        setIconImage(icon.getImage());
    }
}
