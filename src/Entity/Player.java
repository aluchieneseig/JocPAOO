package Entity;

import GameState.GameStateManager;
import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {

    //player stuff
    private int health;
    private int maxHealth;
    private int shot;
    private int maxShot;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    //shot
    private boolean shooting;
    private int shotCost;
    private int shotDamage;
    private ArrayList<Shot> shots;

    //finish level teleport
    private int escape = 0;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {4, 5, 5, 3};

    //animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FIREBALL = 3;

    public Player(TileMap tm) {
        super(tm);

        width = 29;
        height = 49;
        cwidth = 29;
        cheight = 49;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -6.0;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        shot = maxShot = 2500;

        escape = 0;

        shotCost = 200;
        shotDamage = 5;
        shots = new ArrayList<Shot>();

        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Player/player.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 4; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for(int j = 0; j < numFrames[i]; j++) {
                    bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(bi);
           }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }

    public int getHealth() { return health; }

    public int getMaxHealth() { return maxHealth; }

    public void setShooting(boolean b) {
        shooting = b;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {

        //loop through enemies
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            //shots
            for(int j = 0; j < shots.size(); j++) {
                if(shots.get(j).intersects(e)) {
                    e.hit(shotDamage);
                    shots.get(j).setHit();
                    break;
                }
            }

            //check enemy collision
            if(intersects(e)) {
                hit(e.getDamage());
            }
        }
    }

    public void hit(int damage) {
        if(flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    private void getNextPosition() {
        //movement
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
            if(dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0) {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }

        //jumping
        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        //falling
        if(falling) {
            if(dy > 0) dy += fallSpeed + 0.1;
            else dy += fallSpeed;

            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;

            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }


    public void update() {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //check attack has stopped
        if(currentAction == FIREBALL) {
            if(animation.hasPlayedOnce()) shooting = false;
        }

        //shot attack
        shot += 1;
        if(shot > maxShot) shot = maxShot;
        if(shooting && currentAction != FIREBALL) {
            if(shot > shotCost) {
                shot -= shotCost;
                Shot fb = new Shot(tileMap, facingRight);
                fb.setPosition(x, y);
                shots.add(fb);
            }
        }
        //update shots
        for(int i = 0; i < shots.size(); i++) {
            shots.get(i).update();
                if (shots.get(i).shouldRemove()) {
                    shots.remove(i);
                    i--;
                }
        }

        //check done flinching
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000) {
                flinching = false;
            }
        }

        //set animation
        if(shooting) {
            if(currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        }
        if(dy < 0) {
            if(currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }
        else if(left || right) {
            if(currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        //set direction
        if(currentAction != FIREBALL) {
            if(right) facingRight = true;
            if(left) facingRight = false;
        }

        //player damage
        if(damaging) {
            health--;
            setPosition(160, 10);
        }

        //player teleport
        if(escaping) {
            escape = 1;
        }
    }

    public int getEscape() {
        return escape;
    }

    public void stop() {
        left = right = up = down = flinching = jumping = false;
    }

    public void draw(Graphics2D g) {
        setMapPosition();

        //draw shots
        for(int i = 0; i < shots.size(); i++) {
            shots.get(i).draw(g);
        }

        //draw player
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) {
                return;
            }
        }

        super.draw(g);
    }

}
