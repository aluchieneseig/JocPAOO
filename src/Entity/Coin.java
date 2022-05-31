package Entity;

import Entity.*;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Coin extends MapObject {

    private BufferedImage[] sprites;

    public Coin(TileMap tm) {
        super(tm);

        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 20;

        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/coins.png"));
            sprites = new BufferedImage[9];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);

        right = true;
        facingRight = true;

    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //update animation
        animation.update();
    }

    public void draw(Graphics2D g) {
        //if(notOnScreen()) return;

        setMapPosition();

        super.draw(g);
    }

}
