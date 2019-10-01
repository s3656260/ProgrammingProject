package controller;

import model.shareItem;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

public class databaseService {
    public static String DEFAULT_DB = "projectdata.sql";
    public static String TEST_DB = "test_db.sql";
    public static String PURCHASE_TYPE = "PURCHASE";
    public static String SELL_TYPE = "SELL";

    private final String OWNED_STOCK_TABLE = "ownedstocks";
    private final String USER_ID_FIELD = "user_id";
    private final String SYMBOL_FIELD = "symbol";
    private final String AMOUNT_FIELD = "amount";

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

    public void inititialiseTables(){
        //WARNING!!!! RUNNING this will erase all tables
        //use update table method to updat tables to a new format TODO: add update method
        this.mkOwnedStockTable();
    }

    public void destroyTables(){
        //WARNING!!!! TESTING ONLY do not run this on production
        dropTable(OWNED_STOCK_TABLE);
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

    public void transaction(String user_id,String stock_symbol,int amount,String type){
        insertOwnedStock(user_id,stock_symbol,amount);
    }

    private void insertOwnedStock(String user_id,String stock_symbol,int amount){
        //check if user ownes any of this stock already
        String sql = "SELECT * FROM "+OWNED_STOCK_TABLE+" WHERE "+USER_ID_FIELD+" = '"+user_id+"';";
        String id = null;
        List<shareItem> res = new ArrayList<shareItem>();
        try{
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                id = rs.getString("id");
                shareItem si = new shareItem(rs.getString(SYMBOL_FIELD),rs.getInt(AMOUNT_FIELD));
                res.add(si);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        if (res.size()==0){
            String s = "INSERT INTO "+OWNED_STOCK_TABLE+" ("+USER_ID_FIELD+","+SYMBOL_FIELD+","+AMOUNT_FIELD+") VALUES('"+user_id+"','"+stock_symbol+"',"+amount+");";
            execute(s);
        }
        else if(res.size() == 1){
            if(amount == 0){
                String s = "DELETE FROM " + OWNED_STOCK_TABLE + " WHERE id = " + id + ";";
                execute(s);
            }else {
                String s = "UPDATE " + OWNED_STOCK_TABLE + " SET " + AMOUNT_FIELD + " = " + amount + " WHERE id = " + id + ";";
                execute(s);
            }
        }
    }
    public List<shareItem> getUserStocks(String user_id){
        String sql = "SELECT * FROM "+OWNED_STOCK_TABLE+" WHERE "+USER_ID_FIELD+" = '"+user_id+"';";
        List<shareItem> res = null;
        try{
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
                // loop through the result set
                while (rs.next()) {
                    res.add(new shareItem(rs.getString(SYMBOL_FIELD),rs.getInt(AMOUNT_FIELD)));
                }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int getAmountUserOwnes(String user_id, String symbol){
        //gets amount user ownes of specific stock TODO: add functions to get total list of owned stocks
        String sql = "SELECT * FROM "+OWNED_STOCK_TABLE+" WHERE "+USER_ID_FIELD+" = '"+user_id+"' AND "+SYMBOL_FIELD+" = '"+symbol+"';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            int res = -1;
            while (rs.next()) {
                res = rs.getInt(AMOUNT_FIELD);

            }
            return res;
        }catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
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
    private void dropTable(String tableName){
        execute("DROP TABLE IF EXISTS "+tableName+";");
    }
    public void mkOwnedStockTable(){
        //vars to have, user id, stock symbol, owned amount
        execute("DROP TABLE IF EXISTS "+OWNED_STOCK_TABLE+";");
        String query = "CREATE TABLE IF NOT EXISTS "+ OWNED_STOCK_TABLE +" ( id integer PRIMARY KEY, "+USER_ID_FIELD+" text NOT NULL, "+SYMBOL_FIELD+" text NOT NULL, "+AMOUNT_FIELD+" integer );";
        execute(query);
    }
}
