package main;

import Scenes.TitleScene;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1");
        GamePanel gp = new GamePanel(args);
        gp.changeScene(new TitleScene(gp));
    }
}