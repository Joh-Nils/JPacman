package Tiles;

import main.GamePanel;
import util.AssetPool;
import util.SpriteSheet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static util.getURL.getURL;

public class TileManager {
    public static final int tileSize = 8;

    public SpriteSheet tileSprites;

    public int[][] tiles;

    public TileManager() {
        tileSprites = AssetPool.getSpriteSheet(getURL("/images/Tileset.png"),tileSize,tileSize);

        tiles = new int[24][32];

        loadTiles();
    }

    private void loadTiles() {
        try {
            String Level = new String(getURL("/Level/level.txt").openStream().readAllBytes());
            String[] LevelTileRows = Level.split("\n");

            int y = 0;
            for (String Row: LevelTileRows) {
                String[] RowTiles = Row.split(" ");

                for (int i = 0;i < RowTiles.length;i++) {
                    tiles[i][y] = Integer.parseInt(RowTiles[i].trim());
                }

                y++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g, int offsetX, int offsetY) {
        //For Development Dynamically
        //TODO: Static
        for (int y = 0;y < tiles[1].length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                g.drawImage(tileSprites.getSprite(tiles[x][y]), offsetX + x * tileSize * GamePanel.scale, offsetY + y * tileSize * GamePanel.scale, tileSize * GamePanel.scale, tileSize * GamePanel.scale, null);
            }
        }
    }
}
