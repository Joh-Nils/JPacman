package main;

import javax.swing.*;

public class GameFrame extends JFrame {
    GamePanel gp;

    public GameFrame(GamePanel gp) {
        setSize(GamePanel.screenwidth, GamePanel.screenheight);
        setLayout(null);
        setTitle("Kindomfight");
        requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //addKeyListener(new KeyHandler());
        //addMouseListener(new MouseHandler());
        setLocationRelativeTo(null);


        gp.setBounds(0,0, GamePanel.screenwidth, GamePanel.screenheight);
        gp.setVisible(true);
        add(gp);

        addKeyListener(gp.kHandler);

        //addMouseListener(gp.mouseHandler);
        //addMouseMotionListener(gp.mouseHandler);
        //addMouseWheelListener(gp.mouseHandler);

        addWindowListener(gp.wHandler);

        this.gp = gp;

        //setIcon();

        setVisible(true);
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Bilder/ICO.png"));
        setIconImage(icon.getImage());
    }
}
