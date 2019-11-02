package controller;

import model.shareItem;
import model.transaction;
import model.userItem;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class databaseService {
    public static double InitialUserBalance = 1000;

    public static String DEFAULT_DB = "projectdata.sql";
    public static String TEST_DB = "test_db.sql";
    public static String PURCHASE_TYPE = "PURCHASE";
    public static String SELL_TYPE = "SELL";
    public static String OWNED_STOCK_TABLE = "ownedstocks";
    public static String TRANSACTION_TABLE = "transactions";
    public static String USER_TABLE = "users";
    public static String BALANCE_TABLE = "user_balances";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private final String USER_ID_FIELD = "user_id";
    private final String USER_NAME_FIELD = "user_name";
    private final String USER_PASSWORD_FIELD = "password";
    private final String SYMBOL_FIELD = "symbol";
    private final String AMOUNT_FIELD = "amount";
    private final String TYPE_FIELD = "type";
    private final String DATE_TIME_FIELD = "datetime";
    private final String VALUE_FIELD = "value";
    private final String BALANCE_FIELD = "balance";

    private String fileName;
    private String url;
    private Connection conn;
    //
    //                      Test FNs
    //------------------------------------------------------
    public boolean checkTableExists(String tableName){
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<userItem> getAllUsers(){
        List<userItem> res = new ArrayList<>();
        //
        String sql = "SELECT * FROM "+USER_TABLE+";";
        try{
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                String id = rs.getString(USER_ID_FIELD);
                String uName = rs.getString(USER_NAME_FIELD);
                res.add(new userItem(uName,id));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        //
        return res;
    }
    //------------------------------------------------------
    //
    //

    public databaseService(String database){
        fileName = database;
        conn = null;
        this.startDBService();
    }
//jdbc:sqlserver://takestock.database.windows.net:1433;
// database=takestock;
// user=stockadm@takestock;
// password={your_password_here};
// encrypt=true;
// trustServerCertificate=false;
// hostNameInCertificate=*.database.windows.net;loginTimeout=30;
    public void startDBService(){
        String hostName = "takestock.database.windows.net"; // update me
        String dbName = "takestock"; // update me
        String user = "stockadm@takestock"; // update me
        String password = "PASsword123"; // update me
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"+
                "hostNameInCertificate=*.database.windows.net;loginTimeout=30;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
                , hostName, dbName, user, password);
        try {
            conn = DriverManager.getConnection(url);
            String schema = conn.getSchema();
            System.out.println("Successful connection - Schema: " + schema);
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
        //use update table method to update tables to a new format TODO: add update method
        this.mkOwnedStockTable();
        this.mkTransactionTable();
        this.mkUserTable();
        this.mkBalanceTable();
    }

    public void buildIfNone(){
        File tempFile = new File("c:/temp/temp.txt");
        boolean exists = tempFile.exists();
    }

    public void destroyTables(){
        //WARNING!!!! TESTING ONLY do not run this on production
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.startDBService();
        dropTable(OWNED_STOCK_TABLE);
        dropTable(TRANSACTION_TABLE);
        dropTable(USER_TABLE);
        dropTable(BALANCE_TABLE);
    }

    public boolean deleteDatabase(){
        try {
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transaction(String user_id,String stock_symbol,int amount,String type,double value, int change){
        insertOwnedStock(user_id,stock_symbol,amount);
        if(type != null){
            insertToTransactions(user_id,stock_symbol,change,type,value);
        }
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
            if (!rs.next()){
                return 0;
            }else{
                int res;
                do {
                    res = rs.getInt(AMOUNT_FIELD);
                }while (rs.next());
                return res;
            }
            // loop through the result set

        }catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
    }

    public List<transaction> getUserTransactionList(String user_id){
        List<transaction> res = new ArrayList<>();
        String sql = "SELECT * FROM "+TRANSACTION_TABLE+" WHERE "+USER_ID_FIELD+" = '"+user_id+"';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {

                int amount = rs.getInt(AMOUNT_FIELD);
                String symbol = rs.getString(SYMBOL_FIELD);
                String type = rs.getString(TYPE_FIELD);
                String dtString = rs.getString(DATE_TIME_FIELD);
                double value = rs.getDouble(VALUE_FIELD);

                Date parsedDate = sdf.parse(dtString);
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

                res.add(new transaction(user_id,symbol,type, amount,timestamp,value));
            }
            return res;
        }catch (SQLException | ParseException e) {
            e.printStackTrace();

        }
        return null;
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

    public userItem getUserLogin(String userName, String passwords){
        String sql = "SELECT * FROM "+USER_TABLE+" WHERE "+USER_NAME_FIELD+"='"+userName+"' AND "+USER_PASSWORD_FIELD+" = '"+passwords+"';";
        userItem res = null;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                res = new userItem(userName,rs.getString(USER_ID_FIELD));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //res.set_Database(this);
        return res;
    }

    public boolean regesterUser(String username,String password){
        //generate user ID
        String user_id = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
        String sql = "SELECT * FROM "+USER_TABLE+" WHERE "+USER_ID_FIELD+" = '"+user_id+"' OR "+USER_NAME_FIELD+" = '"+username+"';";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()==true) return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //insert user
        sql = "INSERT INTO "+USER_TABLE+" ("+USER_ID_FIELD+","+USER_NAME_FIELD+","+USER_PASSWORD_FIELD+") VALUES('"+user_id+"','"+username+"','"+password+"');";
        execute(sql);
        //make balance table
        sql = "INSERT INTO "+BALANCE_TABLE+" ("+USER_ID_FIELD+","+BALANCE_FIELD+") VALUES('"+user_id+"',"+InitialUserBalance+");";
        execute(sql);
        return true;
    }

    public void updateUserCurrency(String user_id, double balance){
        String sql = "UPDATE "+BALANCE_TABLE+" SET "+BALANCE_FIELD+"="+balance+" WHERE "+USER_ID_FIELD+"='"+user_id+"';";
        execute(sql);
    }

    public double getUserCurrency(String user_id){
        String sql = "SELECT * FROM "+BALANCE_TABLE+" WHERE "+USER_ID_FIELD+"='"+user_id+"';";
        double res = -1;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                res = rs.getDouble(BALANCE_FIELD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void insertToTransactions(String user_id, String symbol, int amount,String type,double value){
        //get timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dt = sdf.format(timestamp);
        String sql = "INSERT INTO "+TRANSACTION_TABLE+" ("+USER_ID_FIELD+","+SYMBOL_FIELD+","+AMOUNT_FIELD+","+DATE_TIME_FIELD+","+TYPE_FIELD+","+VALUE_FIELD+") VALUES('"+user_id+"','"+symbol+"',"+amount+",'"+dt+"','"+type+"',"+value+");";
        execute(sql);
    }

    private void dropTable(String tableName){
        execute("DROP TABLE IF EXISTS "+tableName+";");
    }

    public void mkOwnedStockTable(){
        //vars to have, user id, stock symbol, owned amount
        execute("DROP TABLE IF EXISTS "+OWNED_STOCK_TABLE+";");
        String query = "CREATE TABLE  "+ OWNED_STOCK_TABLE +" ( id integer NOT NULL IDENTITY PRIMARY KEY, "+USER_ID_FIELD+" varchar(50) NOT NULL, "+SYMBOL_FIELD+" varchar(50) NOT NULL, "+AMOUNT_FIELD+" integer );";
        execute(query);
    }

    public void mkTransactionTable(){
        //vars to have, user id, stock symbol, owned amount
        execute("DROP TABLE IF EXISTS "+TRANSACTION_TABLE+";");
        String query = "CREATE TABLE "+ TRANSACTION_TABLE +" ( id integer NOT NULL IDENTITY PRIMARY KEY, "+USER_ID_FIELD+" varchar(50) NOT NULL, "+SYMBOL_FIELD+" varchar(50) NOT NULL, "+AMOUNT_FIELD+" integer,"+DATE_TIME_FIELD+" varchar(50) NOT NULL, "+TYPE_FIELD+" varchar(50) NOT NULL,"+VALUE_FIELD+" real NOT NULL );";
        execute(query);
    }

    public void mkUserTable(){
        //vars to have, user id, stock symbol, owned amount
        execute("DROP TABLE IF EXISTS "+USER_TABLE+";");
        String query = "CREATE TABLE "+ USER_TABLE +" ( id integer NOT NULL IDENTITY PRIMARY KEY, "+USER_ID_FIELD+" varchar(50) NOT NULL,"+USER_NAME_FIELD+" varchar(50) NOT NULL,"+USER_PASSWORD_FIELD+" varchar(50) NOT NULL );";
        execute(query);
    }

    public void mkBalanceTable(){
        //vars to have, user id, stock symbol, owned amount
        execute("DROP TABLE IF EXISTS "+BALANCE_TABLE+";");
        String query = "CREATE TABLE "+ BALANCE_TABLE +" ( id integer NOT NULL IDENTITY PRIMARY KEY, "+USER_ID_FIELD+" varchar(50) NOT NULL,"+BALANCE_FIELD+" real NOT NULL );";
        execute(query);
    }
}
