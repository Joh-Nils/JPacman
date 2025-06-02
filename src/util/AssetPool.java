package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static final Map<URI, SpriteSheet> spriteSheets = new HashMap<>();

    public static SpriteSheet getSpriteSheet(URL url, int SpriteWidth, int SpriteHeight) {
        try {
            if (spriteSheets.containsKey(url.toURI())) {
                return spriteSheets.get(url.toURI());
            }

            SpriteSheet spriteSheet = new SpriteSheet(url,SpriteWidth,SpriteHeight);
            spriteSheets.put(url.toURI(), spriteSheet);
            return spriteSheet;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void free() {
        spriteSheets.clear();
    }
}
