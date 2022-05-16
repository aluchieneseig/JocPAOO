package Entity;

import GameState.GameStateManager;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Coin extends MapObject {

    //game state manager
    private GameStateManager gsm;

    //animations
    private BufferedImage coinImage;

    public Coin(TileMap tm) {
        super(tm);

        width = 29;
        height = 49;
        cwidth = 29;
        cheight = 49;

        //load sprites
        try {
            //incarcare imagine
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Robot/output.png"));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        //update position
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        //g.drawImage();
    }

}
