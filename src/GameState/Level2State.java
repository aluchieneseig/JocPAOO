package GameState;

import Main.DataBase;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Enemies.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class Level2State extends GameState {
    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;

    private static int numCoins = 0;

    private HUD hud;

    DataBase data = DataBase.create();

    public Level2State(GameStateManager gsm) {
        this.gsm = gsm;
        init();

    }
    public void init() {
        //load map
        tileMap = new TileMap(64);
        tileMap.loadTiles("/Platform/Tiles.png");
        tileMap.loadMap("/map2.txt");
        tileMap.setPosition(0, 0);

        //set background
        bg = new Background("/Backgrounds/Background.png", 0.1);

        //player init
        player = new Player(tileMap);
        player.setPosition(160, 10);

        //enemies init
        populateEnemies();

        //coins init
        numCoins = 0;
        populateCoins();

        //hud init
        hud = new HUD(player);
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        BlackEvilRobot s;
        Point[] points = new Point[] {
                new Point(220, 10),
                new Point(1108, 10),
                new Point(1725, 10),
                new Point(3008, 10)};
        for(int i = 0; i < points.length; i++) {
            s = new BlackEvilRobot(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
    }

    private void populateCoins() {
        coins = new ArrayList<Coin>();
        Coin s;
        Point[] points = new Point[] {
                new Point(224, 288),
                new Point(288, 288),
                new Point(352, 288),
                new Point(480, 224),
                new Point(608, 224),
                new Point(672, 224),
                new Point(800, 224),
                new Point(928, 224),
                new Point(1056, 160),
                new Point(1376, 160),
                new Point(1440, 224),
                new Point(1504, 288),
                new Point(1568, 352),
                new Point(1824, 352),
                new Point(1952, 288),
                new Point(2080, 288),
                new Point(2208, 288),
                new Point(2272, 352),
                new Point(2400, 288),
                new Point(2528, 288),
                new Point(2656, 224),
                new Point(2720, 160),
                new Point(2784, 96),
                new Point(2912, 160),
                new Point(2976, 160)};
        for(int i = 0; i < points.length; i++) {
            s = new Coin(tileMap);
            s.setPosition(points[i].x, points[i].y);
            coins.add(s);
        }
    }

    public void update() throws SQLException {

        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        player.checkTileMapCollision();
        if(player.getHealth() == 0) {
            init();
        }

        //attack enemies
        player.checkAttack(enemies);

        //update all enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
            if(enemies.get(i).isDead()) {
                enemies.remove(i);
                i--;
            }
        }

        //update all coins
        for(int i = 0; i < coins.size(); i++) {
            coins.get(i).update();
            if(player.intersects(coins.get(i))) {
                coins.remove(i);
                i--;
                numCoins++;
                data.addRecord(numCoins, 2, 25);
            }
        }
    }
    public void draw(Graphics2D g) {
        //draw bg
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw player
        player.draw(g);

        //draw enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        //draw hud
        hud.draw(g);

        //draw coins
        for(int i = 0; i < coins.size(); i++) {
            coins.get(i).draw(g);
        }

        Font font = new Font("Arial", Font.PLAIN, 32);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(numCoins + "/25", 53, 78 );

    }


    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setJumping(true);
        if(k == KeyEvent.VK_SPACE) player.setShooting(true);
        if(k == KeyEvent.VK_ESCAPE) System.exit(1);
        if(k == KeyEvent.VK_DOWN && numCoins == 25 && player.getEscape() == 1) gsm.setState(GameStateManager.LEVEL3STATE);
        //if(k == KeyEvent.VK_DOWN) gsm.setState(GameStateManager.LEVEL3STATE);
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setJumping(false);
        if(k == KeyEvent.VK_SPACE) player.setShooting(false);
    }
}
