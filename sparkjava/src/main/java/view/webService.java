package view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.apiService;
import controller.databaseService;
import model.shareItem;
import model.userItem;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    //
    //                            test functions
    //------------------------------------------------------------------------------
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

    //------------------------------------------------------------------------------
    //
    //
    public webService(String serviceName, String serviceAction, databaseService db) throws IOException {
        _serviceAction = serviceAction;
        _serviceName = serviceName;
        _apiService = new apiService();
        CurrentUser = new userItem(10000);
        StockList = new ArrayList<JSONObject>();
        genStocklist();
        haveList = false;
    }
    public void testFn(){
        doPurchase("OHI","String userId", 12);
        doPurchase("OHI","String userId", 1);
    }

    public void stopService(){
        stop();
    }

    public void startService(){
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        //pricecheck api
        String pathStr = "/"+_serviceName+"/sym/:name";
        get(pathStr, (req, res) -> {
            String name = req.params(":name");
            shareItem quote = _apiService.getBySymb(name);
            return quote.get_price();
        });
        //top share list
        pathStr = "/"+_serviceName+"/top";
        get(pathStr, (req, res) -> getTop());
        pathStr = "/"+_serviceName+"/userCash/:userId";
        get(pathStr, (req, res) -> getUserMoney());
        pathStr = "/"+_serviceName+"/userPurchase/";
        post(pathStr, (req, res) -> {
            res.type("application/json");

            JSONObject bod = new JSONObject(req.body());

            String sym = bod.getString("sym");
            String id = "1";
            int amount = bod.getInt("amount");
            doPurchase(sym,id,amount);
            return 200;
        });

    }

    private boolean doShareSale(String sym,String userId, int amount){
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
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doPurchase(String sym,String userId, int amount){
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
                    return true;
                }

            }
            return false;
        }
    }

    private JSONObject getUserMoney(){
        double val = CurrentUser.get_Money();
        JSONObject json = new JSONObject();
        json.put("userMoney",val);
        return json;
    }

    private int checkForUserStock(String symbol){
        //loop through array
        int res = 0;
        for (JSONObject json: StockList) {
            String symC = json.getString("symbol");
            if((symC).equals(symbol)){
                res= json.getInt("value");
            }
        }
        //find stocks
        return res;
    }

    private void genStocklist() throws IOException {
        list = new JsonArray();
        allShares  = _apiService.genList();
        for (shareItem x :allShares) {
            list.add(x.toJson());
        }
    }

    private Object getTop() throws IOException {
        return list;
    }

    public Object addUserAmounts(JsonArray list){
        JsonParser jsonParser = new JsonParser();
        // Convert JSON Array String into JSON Array
        String jsonArrayString = "[\n" +
                "            {\"symbol\":\"GE\",\"company\":\"General Electric Co.\",\"price\":\"7.93\"},\n" +
                "            {\"symbol\":\"MO\",\"company\":\"Altria Group, Inc.\",\"price\":\"45.25\"},\n" +
                "            {\"symbol\":\"CHK\",\"company\":\"Chesapeake Energy Corp.\",\"price\":\"1.39\"},\n" +
                "            {\"symbol\":\"AMD\",\"company\":\"Advanced Micro Devices, Inc.\",\"price\":\"30.2\"},\n" +
                "            {\"symbol\":\"BAC\",\"company\":\"Bank of America Corp.\",\"price\":\"26.47\"},\n" +
                "            {\"symbol\":\"PM\",\"company\":\"Philip Morris International, Inc.\",\"price\":\"71.7\"},\n" +
                "            {\"symbol\":\"T\",\"company\":\"AT&T, Inc.\",\"price\":\"34.72\"},\n" +
                "            {\"symbol\":\"SNAP\",\"company\":\"Snap, Inc.\",\"price\":\"15.51\"},\n" +
                "            {\"symbol\":\"NLY\",\"company\":\"Annaly Capital Management, Inc.\",\"price\":\"8.42\"},\n" +
                "            {\"symbol\":\"ECA\",\"company\":\"Encana Corp.\",\"price\":\"4.31\"}]";

        JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();
        JsonArray jArray = new JsonArray();
        JsonObject listJson = new JsonObject();

        for (JsonElement x :arrayFromString) {

            JsonObject pack = new JsonObject();
            JsonObject y = x.getAsJsonObject();
            String sym = y.get("symbol").toString();
            int userAmount = checkForUserStock(sym);

            pack.addProperty("symbol",sym);
            pack.addProperty("company",y.get("company").toString());
            pack.addProperty("price",y.get("price").toString());
            pack.addProperty("userStock",userAmount);

            jArray.add(pack);
        }
        listJson.add("items",jArray);
        return listJson;
    }
}
