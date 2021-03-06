package view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.apiService;
import controller.databaseService;
import model.shareItem;
import model.transaction;
import model.userItem;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static controller.databaseService.PURCHASE_TYPE;
import static controller.databaseService.SELL_TYPE;
import static spark.Spark.*;

public class webService {

    private String _serviceName;
    private String _serviceAction;
    private apiService _apiService;
    private userItem CurrentUser;
    private ArrayList<JSONObject> StockList;
    private List<shareItem> allShares;
    private JsonArray list;
    private boolean haveList;
    private databaseService database;
    private List<transaction> userTransactions;
    //
    //                            test functions
    //------------------------------------------------------------------------------

    public List<transaction> getUserTransactions() {
        genTransactionList();
        return userTransactions;
    }

    public String get_serviceName() { return _serviceName; }

    public String get_serviceAction() { return _serviceAction; }

    public apiService get_apiService() { return _apiService; }

    public userItem getCurrentUser() { return CurrentUser; }

    public ArrayList<JSONObject> getStockList() { return StockList; }

    public List<shareItem> getAllShares() { return allShares; }

    public JsonArray getList() { return list; }

    public boolean isHaveList() { return haveList; }

    public boolean testPurchase(String sym,String userId, int amount){ return doPurchase(sym,userId,amount); }

    public shareItem getTestShare(int index){ return allShares.get(index); }

    public int stockCount(int index){
        JSONObject o = new JSONObject(list.get(index).toString());
        return o.getInt("uAmount");
    }
    public void setUserMoney(double amount){
        CurrentUser.set_Money(amount);
    }

    public void resetList(){ list = null; }

    public void testGenList() throws IOException {genStocklist();}

    public JSONObject getTestListStock(int index){ return new JSONObject(list.get(index).toString()); }

    public void testAddStockOwnership(int index,int amount){
        JSONObject o = new JSONObject(list.get(index).toString());
        int nAmount = o.getInt("uAmount") + amount;
        list.get(index).getAsJsonObject().addProperty("uAmount", nAmount);
        JSONObject n = new JSONObject(list.get(index).toString());

    }
    public List<transaction> testTransactionList(){
        genTransactionList();
        return userTransactions;
    }
    public JsonArray getStocksOwned(){
        JsonArray res = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject o = new JSONObject(list.get(i).toString());
            int uAmount = (int) o.get("uAmount");
            if (uAmount >= 1) {
                res.add(list.get(i));
            }
        }
        if(res.size() == 0) return null;
        else return res;
    }

    public boolean testSale(String sym,String userId, int amount){ return doShareSale(sym,userId,amount); }

    public databaseService getDBservice(){ return database; }

    //------------------------------------------------------------------------------
    //
    //

    public webService(String serviceName, String serviceAction, databaseService db) throws IOException {//test constructor
        _serviceAction = serviceAction;
        _serviceName = serviceName;
        database = db;
        _apiService = new apiService();
        CurrentUser = new userItem(10000,"1",db);
        StockList = new ArrayList<JSONObject>();
        genStocklist();
        haveList = false;
    }

    webService(String serviceName, databaseService db,userItem user) throws IOException {
        _serviceAction = null;
        _serviceName = serviceName;
        database = db;
        _apiService = new apiService();
        CurrentUser = user;
        //CurrentUser.add_money(10000);
        StockList = new ArrayList<JSONObject>();
        genStocklist();
        haveList = false;
    }

    public JsonArray userTransList(){
        genTransactionList();
        JsonArray res = new JsonArray();
        for (transaction x: userTransactions) {
            res.add(x.toJson());
        }
        return res;
    }

    private void genTransactionList(){
        userTransactions = database.getUserTransactionList(CurrentUser.get_user_id());
    }

    public boolean doShareSale(String sym,String userId, int amount){
        shareItem q = _apiService.getBySymb(sym);
        double price = Double.parseDouble(q.get_price());
        double cost = price*amount;
        for (int i = 0; i < list.size(); i++) {
            JSONObject o = new JSONObject(list.get(i).toString());
            String s = (String) o.get("symbol");
            if (s.equals(sym)) {
                int oAmnt = o.getInt("uAmount");
                if (oAmnt >= amount){
                    int nAmount = oAmnt - amount;
                    list.get(i).getAsJsonObject().addProperty("uAmount", nAmount);
                    JSONObject n = new JSONObject(list.get(i).toString());
                    CurrentUser.add_money(cost);
                    database.transaction(userId,s,nAmount,SELL_TYPE,cost,amount);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doPurchase(String sym,String userId, int amount){
        shareItem q = _apiService.getBySymb(sym);
        double price = Double.parseDouble(q.get_price());
        double cost = price*amount;
        double userM = CurrentUser.get_Money();
        if(cost>userM){
            //check if user has enough money for purchase
            return false;
        }else {
            CurrentUser.rmv_Money(cost);
            for (int i = 0; i < list.size(); i++) {
                JSONObject o = new JSONObject(list.get(i).toString());
                String s = (String) o.get("symbol");
                if (s.equals(sym)) {
                    int nAmount = o.getInt("uAmount") + amount;
                    list.get(i).getAsJsonObject().addProperty("uAmount", nAmount);
                    JSONObject n = new JSONObject(list.get(i).toString());
                    database.transaction(userId,s,nAmount,PURCHASE_TYPE,cost,amount);
                    return true;
                }

            }
            return false;
        }
    }
    private int checkForUserStock(String symbol){
        //returns amount database user holds
        return this.database.getAmountUserOwnes(CurrentUser.get_user_id(),symbol);
    }

    private void genStocklist() throws IOException {
        list = new JsonArray();
        allShares  = _apiService.genList();
        List<shareItem> resultSet = this.database.getUserStocks(CurrentUser.get_user_id());
        System.out.println("point");
        boolean noRes = (resultSet == null);
        boolean match = false;

        for (shareItem x :allShares) {
            //get amount of stock for each symbol from db, afterwards any transaction should be synced to reflect the db
            //x.set_amount(checkForUserStock(x.getSymbol()));
            if (!noRes){
                for (shareItem resI :resultSet) {
                    if (resI.getSymbol().equals(x.getSymbol())){
                        x.set_amount(resI.get_amount());
                        match = true;
                        break;
                    }
                }
            }
            if (!match){
                x.set_amount(0);
            }
            list.add(x.toJson());
            match = false;
        }

    }

    Object getTop() throws IOException {
        return list;
    }
}
