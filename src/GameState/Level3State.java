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

public class Level3State extends GameState {
    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;

    private static int numCoins = 0;

    private HUD hud;

    DataBase data = DataBase.create();

    public Level3State(GameStateManager gsm) {
        this.gsm = gsm;
        init();

    }
    public void init() {
        //load map
        tileMap = new TileMap(64);
        tileMap.loadTiles("/Platform/Tiles.png");
        tileMap.loadMap("/map3.txt");
        tileMap.setPosition(0, 0);

        //set background
        bg = new Background("/Backgrounds/Background.png", 0.1);

        //player init
        player = new Player(tileMap);
        player.setPosition(160, 10);

        //enemies init
        //populateEnemies1();
        //populateEnemies2();
        populateEnemies();

        //coins init
        numCoins = 0;
        populateCoins();

        //hud init
        hud = new HUD(player);
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        BlackEvilRobot b1 = new BlackEvilRobot(tileMap);
        b1.setPosition(860, 10);
        BlackEvilRobot b2 = new BlackEvilRobot(tileMap);
        b2.setPosition(1760, 10);
        BlackEvilRobot b3 = new BlackEvilRobot(tileMap);
        b3.setPosition(2656, 10);
        MaleficentRobot m1 = new MaleficentRobot(tileMap);
        m1.setPosition(800, 10);
        MaleficentRobot m2 = new MaleficentRobot(tileMap);
        m2.setPosition(1952, 10);
        MaleficentRobot m3 = new MaleficentRobot(tileMap);
        m3.setPosition(2208, 10);
        MaleficentRobot m4 = new MaleficentRobot(tileMap);
        m4.setPosition(2720, 10);
        MaleficentRobot m5 = new MaleficentRobot(tileMap);
        m5.setPosition(3744, 10);
        MaleficentRobot m6 = new MaleficentRobot(tileMap);
        m6.setPosition(3808, 10);
        enemies.add(b1);
        enemies.add(b2);
        enemies.add(b3);
        enemies.add(m1);
        enemies.add(m2);
        enemies.add(m3);
        enemies.add(m4);
        enemies.add(m5);
        enemies.add(m6);
    }

    private void populateCoins() {
        coins = new ArrayList<Coin>();
        Coin s;
        Point[] points = new Point[] {
                new Point(288, 224),
                new Point(416, 160),
                new Point(544, 160),
                new Point(672, 160),
                new Point(928, 160),
                new Point(1056, 224),
                new Point(1184, 288),
                new Point(1312, 288),
                new Point(1376, 288),
                new Point(1440, 224),
                new Point(1504, 160),
                new Point(1632, 96),
                new Point(1888, 160),
                new Point(1952, 160),
                new Point(2016, 160),
                new Point(2272, 96),
                new Point(2400, 160),
                new Point(2528, 224),
                new Point(2848, 224),
                new Point(2976, 160),
                new Point(3360, 160),
                new Point(3488, 160),
                new Point(3616, 224),
                new Point(3808, 288),
                new Point(3872, 288)};
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
                data.addRecord(numCoins, 3, 25);
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


    public void keyPressed(int k) throws SQLException {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setJumping(true);
        if(k == KeyEvent.VK_SPACE) player.setShooting(true);
        if(k == KeyEvent.VK_ESCAPE) System.exit(1);
        if(k == KeyEvent.VK_DOWN && numCoins == 25 && player.getEscape() == 1){
            gsm.setState(GameStateManager.MENUSTATE);
            data.afisare();
            data.close();

        }
        //if(k == KeyEvent.VK_DOWN) gsm.setState(GameStateManager.MENUSTATE);
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setJumping(false);
        if(k == KeyEvent.VK_SPACE) player.setShooting(false);
    }
}
