package TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    private BufferedImage image;
    private int type;

    //tile types
    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;
    public static final int DAMAGE = 2;
    public static final int ESCAPE = 3;
    public static final int COLLECT = 4;

    public Tile(BufferedImage image, int type) {
        this.image = image;
        this.type = type;
    }

    public void setImage(BufferedImage image) { this.image = image; }
    public BufferedImage getImage() {
        return image;
    }
    public void setType(int type) { this.type = type; }
    public int getType() { return type; }

    public static BufferedImage resizeImage(final BufferedImage image, int width, int height) {
        int targetw = 0;
        int targeth = 75;

        if (width > height)targetw = 112;
        else targetw = 50;

        do {
            if (width > targetw) {
                width /= 2;
                if (width < targetw) width = targetw;
            }

            if (height > targeth) {
                height /= 2;
                if (height < targeth) height = targeth;
            }
        } while (width != targetw || height != targeth);

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }
}
