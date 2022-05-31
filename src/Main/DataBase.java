package Main;

import java.sql.*;

public class DataBase {
    private static DataBase db = null;
    private Connection conn = null;
    private static Statement stmt = null;

    private DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:JOC.db");
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "CREATE TABLE ROBORUN " +
                    "(numCoin INT NOT NULL, " +
                    "Level INT NOT NULL, " +
                    "numMaxCoins INT NOT NULL)";
            stmt.execute(sql);
            stmt.close();
            conn.commit();
            //conn.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully! ");
    }

    public static void afisare() throws SQLException {
        ResultSet rs = stmt.executeQuery( "SELECT * FROM ROBORUN;" );
        while ( rs.next() ) {
            int numCoin = rs.getInt("numCoin");
            int level = rs.getInt("Level");
            int numMaxCoins = rs.getInt("numMaxCoins");
            System.out.print("numCoin = " + numCoin);
            System.out.print(", Level = " + level);
            System.out.print(", numMaxCoins = " + numMaxCoins + "\n");

        }
        rs.close();
    }

    public static DataBase create() {
        if(db == null) {
            db = new DataBase();
        }
        return db;
    }

    public void close() {
        if(conn != null) {
            try {
                stmt.close();
                conn.close();
            }
            catch(Exception e) {}
        }
    }

    public void addRecord(int numCoin, int level, int numMaxCoins) throws SQLException {
        String sql = "INSERT INTO ROBORUN (numCoin, Level, numMaxCoins) " + "VALUES (" + numCoin + ", " + level + ", " + numMaxCoins +");";
        stmt.executeUpdate(sql);
    }
}
