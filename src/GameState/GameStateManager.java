package GameState;

import Main.GamePanel;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;


    public static final int NUMGAMESTATES = 4;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public static final int LEVEL2STATE = 2;
    public static final int LEVEL3STATE = 3;

    public GameStateManager() {

        gameStates = new GameState[NUMGAMESTATES];

        currentState = MENUSTATE;
        loadState(currentState);
    }

    private void loadState(int state) {
        if(state == MENUSTATE)
            gameStates[state] = new MenuState(this);
        else if(state == LEVEL1STATE)
            gameStates[state] = new Level1State(this);
        else if(state == LEVEL2STATE)
            gameStates[state] = new Level2State(this);
        else if(state == LEVEL3STATE)
            gameStates[state] = new Level3State(this);
    }

    private void unloadState(int state) { gameStates[state] = null; }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() throws SQLException {
        if(gameStates[currentState] != null) {
            gameStates[currentState].update();
        }
    }
    public void draw(java.awt.Graphics2D g) {
        if(gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
    }
    public void keyPressed(int k) throws SQLException {
        gameStates[currentState].keyPressed(k);
    }
    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }
}
