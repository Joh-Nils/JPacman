package Scenes;

import java.awt.*;

public interface Scene {

    void initialize();
    void update(float dt);
    void draw(Graphics2D g);
}
