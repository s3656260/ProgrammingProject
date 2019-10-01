package controller;

import model.shareItem;

import java.io.File;
import java.sql.*;

public class databaseService {
    public static String DEFAULT_DB = "projectdata.sql";
    public static String TEST_DB = "test_db.sql";
    private final String OWNED_STOCK_TABLE = "ownedstocks";
    private final String USER_ID_FIELD = "user_id";
    private String fileName;
    private String url;
    private Connection conn;
    //
    //                      Test FNs
    //------------------------------------------------------

    //------------------------------------------------------
    //
    //

    public databaseService(String database){
        fileName = database;
        url = "jdbc:sqlite:database/" + fileName;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Successfully connected to database "+ fileName);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean deleteDatabase(){
        try {
            conn.close();
            return new File(url).delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addStockPerchase(String user_id,String stock_symbol,int amount_purchased,){
        this.executeInsert("");
    }

    private void insertOwnedStock(String user_id,String stock_symbol,int amount_purchased){
        //check if user ownes any of this stock already
        String sql = "";
    }

    public list<shareItem> getUserStocks(String user_id){
        String sql = "SELECT * FROM "+OWNED_STOCK_TABLE+" WHERE ";
    }

    private void execute(String statment){
        //works for table make, insert,
        String sql = statment;
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mkOwnedStockTable(){
        //vars to have, user id, stock symbol, owned amount
        String query = "CREATE TABLE IF NOT EXISTS "+ this.OWNED_STOCK_TABLE +" ( user_id integer PRIMARY KEY, symbol text NOT NULL, amount integer );";
        execute(query);
    }
}
