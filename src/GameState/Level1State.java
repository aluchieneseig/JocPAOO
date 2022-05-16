package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }
    public void init() {
        tileMap = new TileMap(64);
        tileMap.loadTiles("/Platform/png/Tiles/tiles9.png");
        tileMap.loadMap("/map.txt");
        tileMap.setPosition(0, 0);

        bg = new Background("/Backgrounds/Background1.png", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 10);
    }
    public void update() {

        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        player.checkTileMapCollision();
        //if() {
        //    gsm.setState(GameStateManager.LEVEL2STATE);
        //}
    }
    public void draw(Graphics2D g) {
        //draw bg
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw player
        player.draw(g);
    }
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setJumping(true);
        //if(k == KeyEvent.VK_E) player.setGliding(true);
        //if(k == KeyEvent.VK_R) player.setScratching();
        if(k == KeyEvent.VK_F) player.setFiring();
        if(k == KeyEvent.VK_ESCAPE) System.exit(1);
        if(k == KeyEvent.VK_DOWN) gsm.setState(GameStateManager.LEVEL2STATE);
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setJumping(false);
    }
}
