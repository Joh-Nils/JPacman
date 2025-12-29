package Scenes;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class TitleScene implements Scene {
    private final GamePanel gp;
    private final PlayingScene playingScene;

    private BufferedImage FrameBuffer = new BufferedImage(GamePanel.screenwidth,GamePanel.screenheight,BufferedImage.TYPE_INT_ARGB);
    private Graphics2D fbg = FrameBuffer.createGraphics();
    private BufferedImage postProcessedFrameBuffer;

    public TitleScene(GamePanel gp) {
        this.gp = gp;
        this.playingScene = new PlayingScene(gp);
    }

    @Override
    public void initialize() {
        gp.setBackground(new Color(6,1,13));

        playingScene.initialize();
        fbg.setColor(gp.getBackground());
        fbg.fillRect(0,0,FrameBuffer.getWidth(), FrameBuffer.getHeight());
        playingScene.drawWithoutUI(fbg);
        postProcessedFrameBuffer = blur(FrameBuffer);
    }

    @Override
    public void reset() {
        reScale();
    }

    @Override
    public void reScale() {
        FrameBuffer = new BufferedImage(GamePanel.screenwidth,GamePanel.screenheight,BufferedImage.TYPE_INT_ARGB);
        fbg = FrameBuffer.createGraphics();
        fbg.setColor(gp.getBackground());
        fbg.fillRect(0,0,FrameBuffer.getWidth(), FrameBuffer.getHeight());
        playingScene.drawWithoutUI(fbg);
        postProcessedFrameBuffer = blur(FrameBuffer);

        playingScene.ScoreFont = playingScene.ScoreFont.deriveFont((float) (8 * GamePanel.scale));
    }

    @Override
    public void update(float dt) {
        if (gp.kHandler.somethingPressed) {
            gp.changeScene(playingScene);
        }
    }

    @Override
    public void draw(Graphics2D g) {

        g.drawImage(postProcessedFrameBuffer, 0, 0, null);

        g.setFont(playingScene.ScoreFont.deriveFont(Font.PLAIN, (float) (33 * GamePanel.scale)));

        int x = (int) ((double) GamePanel.screenwidth /2 - g.getFontMetrics().getStringBounds("JP cman",g).getWidth()/2);
        int y = (int) (GamePanel.screenheight/3);

        g.setColor(Color.GRAY);
        g.drawString("JP cman", (int) (x + 2 * GamePanel.scale), (int) (y + 2 * GamePanel.scale));
        g.setColor(Color.WHITE);
        g.drawString("JP cman", x, y);

        g.drawImage(gp.player.getSpriteSheet().getSprite(6), (int) (x + g.getFontMetrics().getStringBounds("JP",g).getWidth()), (int) (y - g.getFontMetrics().getAscent() + g.getFontMetrics().getHeight()/2 - (int) (16 * GamePanel.scale * 2/2)), (int) (16 * GamePanel.scale * 2), (int) (16 * GamePanel.scale * 2), null);

        g.setFont(playingScene.ScoreFont);

        g.drawString("Press any key to start", (int) ((double) GamePanel.screenwidth /2 - g.getFontMetrics().getStringBounds("Press any key to start",g).getWidth()/2), GamePanel.screenheight/3*2);

        g.setColor(Color.GRAY);
        g.setFont(playingScene.ScoreFont.deriveFont(Font.PLAIN, (float) (6 * GamePanel.scale)));
        g.drawString("© 2025 JohNils", (int) (GamePanel.screenwidth - g.getFontMetrics().getStringBounds("© 2025 JohNils",g).getWidth() - 5 * GamePanel.scale), (int) (GamePanel.screenheight - 5 * GamePanel.scale - g.getFontMetrics().getDescent()));
    }


    public static BufferedImage blur(BufferedImage image) {
        // Simple 3x3 Gaussian kernel
        float[] matrix = {
                1, 2, 1,
                2, 4, 2,
                1, 2, 1
        };
        for (int i = 0; i < matrix.length; i++) matrix[i] /= 16f;

        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage blurred = image;
        for (int i = 0; i < 10; i++) { // increase passes for stronger blur
            blurred = op.filter(blurred, null);
        }
        return blurred;
    }
}
