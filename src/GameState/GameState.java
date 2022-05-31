package GameState;

import java.sql.SQLException;

public abstract class GameState {
    protected GameStateManager gsm;
    public abstract void init();
    public abstract void update() throws SQLException;
    public abstract void draw(java.awt.Graphics2D g);
    public abstract void keyPressed(int k) throws SQLException;
    public abstract void keyReleased(int k);

}
