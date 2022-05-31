package GameState;

import Main.DataBase;
import Main.GamePanel;
import TileMap.*;
import Entity.*;

import java.sql.SQLException;
import java.sql.Statement;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Coin> coins;

    private static int numCoins = 0;

    private HUD hud;

    DataBase data = DataBase.create();

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }
    public void init() {
        //load map
        tileMap = new TileMap(64);
        tileMap.loadTiles("/Platform/Tiles.png");
        tileMap.loadMap("/map1.txt");
        tileMap.setPosition(0, 0);
        tileMap.setTween(0.07);

        //set background
        bg = new Background("/Backgrounds/Background.png", 0.1);

        //player init
        player = new Player(tileMap);
        player.setPosition(160, 10);

        //coins init
        numCoins = 0;
        populateCoins();

        //hud init
        hud = new HUD(player);

    }

    private void populateCoins() {
        coins = new ArrayList<Coin>();
        Coin s;
        Point[] points = new Point[] {
                new Point(96, 352),
                new Point(416, 288),
                new Point(544, 224),
                new Point(672, 160),
                new Point(736, 96),
                new Point(800, 96),
                new Point(864, 96),
                new Point(800, 352),
                new Point(928, 288),
                new Point(1056, 288),
                new Point(1184, 352),
                new Point(1248, 288),
                new Point(1312, 224),
                new Point(1440, 224),
                new Point(1568, 224),
                new Point(1696, 224),
                new Point(1760, 288),
                new Point(1824, 352),
                new Point(1952, 288),
                new Point(2080, 224),
                new Point(2272, 288),
                new Point(2336, 352),
                new Point(2400, 352),
                new Point(2400,96),
                new Point(2464, 96)};

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
             //data.afisare();
             //data.close();
             //data.finalize();

         }
        //update all coins
        for(int i = 0; i < coins.size(); i++) {
            coins.get(i).update();
            if(player.intersects(coins.get(i))) {
                coins.remove(i);
                i--;
                numCoins++;
                data.addRecord(numCoins, 1, 25);
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

        //draw hud
        hud.draw(g);

        //draw coins
        for(int i = 0; i < coins.size(); i++) {
            coins.get(i).draw(g);
        }

        //draw numCoins
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
        if(k == KeyEvent.VK_DOWN && numCoins == 25 && player.getEscape() == 1) gsm.setState(GameStateManager.LEVEL2STATE);
        //if(k == KeyEvent.VK_DOWN) gsm.setState(GameStateManager.LEVEL2STATE);
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setJumping(false);
        if(k == KeyEvent.VK_SPACE) player.setShooting(false);
    }
}
