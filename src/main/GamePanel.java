package main;

import Entities.Coin;
import Entities.Ghost;
import Entities.Pacman;
import Handlers.KeyHandler;
import Handlers.ResizeHandler;
import Handlers.WindowHandler;
import Scenes.PlayingScene;
import Scenes.Scene;
import Scenes.TitleScene;
import Tiles.TileManager;
import util.SpriteSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public static final int Padding = 50;

    public static final int LevelWidth = 259;
    public static final int LevelHeight = 330;

    public static final int Width = LevelWidth + Padding * 2; //Padding on each side
    public static final int Height = LevelHeight;

    public static final double ASPECT_RATIO = (double) Width / Height;

    public static double scale = 3;

    //Size
    public static int screenwidth = (int) (scale * Width);
    public static int screenheight = (int) (scale * Height);

    public static int animationSpeeds = 15; //Frames per Second

    public static boolean debug = false;

    //System
    public final WindowHandler wHandler = new WindowHandler();
    public final ResizeHandler rHandler = new ResizeHandler(this);
    public final KeyHandler kHandler = new KeyHandler(this);
    public final GameFrame gf;
    public Thread gameThread;


    public static int FPS = 60;

    //Actors
    public Pacman player;
    public Ghost[] ghosts;
    public Coin[] coins;

    public Scene currentScene = new TitleScene(this);

    public TileManager tileManager = new TileManager();

    public AStar astar = new AStar(this);

    //FPS calc
    long firstFrame;
    int frames;
    long currentFrame;
    int fps = 0;


    public GamePanel(String[] args) {
        //Parse Arguments by looping over them and checking
        for (int i = 0;i < args.length;i++) {
            switch (args[i]) {
                case "-FPS" -> {
                    assert i + 1 < args.length : "Missing Argument Value of FPS";
                    FPS = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "-Width" -> {
                    assert i + 1 < args.length : "Missing Argument Value of Width";
                    screenwidth = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "-Height" -> {
                    assert i + 1 < args.length : "Missing Argument Value of Height";
                    screenheight = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "-Debug" -> {
                    debug = true;
                }
            }
        }

        currentScene.initialize();

        gf = new GameFrame(this);
        start();
    }


    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double beginTime = System.nanoTime() / 1e9;
        double endTime;
        float dt = 0.0f;
        while(gameThread != null) {

            if (FPS <= 0 || System.nanoTime() / 1e9 - beginTime > 1f / FPS) {
                if (dt >= 0) {
                    update(dt);

                    repaint();
                }

                endTime = System.nanoTime() / 1e9;
                dt = (float) (endTime - beginTime);
                beginTime = endTime;
            }
        }
    }

    public void changeScene(Scene newScene) {
        assert newScene != null : "New Scene cant be null";

        newScene.initialize();

        this.currentScene = newScene;
    }

    public void reset() {
        currentScene.reset();
    }

    public void update(float dt) {
        currentScene.update(dt);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        currentScene.draw(g2);

        if (GamePanel.debug) {
            frames++;
            currentFrame = System.currentTimeMillis();
            if(currentFrame > firstFrame + 1000){
                firstFrame = currentFrame;
                fps = frames;
                frames = 0;
            }

            System.out.println("FPS: " + fps);
        }
    }
}
