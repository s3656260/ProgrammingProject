package view;

import com.google.gson.JsonArray;
import controller.apiService;
import controller.databaseService;
import model.shareItem;
import model.userItem;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class userService {
    public static String DEFULT_LOGIN_API = "loginService";
    private String _serviceName;
    private List<webService> _sessions;
    private databaseService _database;
    private apiService _apiService;
    private List<userItem> loggedUsers;

    //--TEST FUNCTIONS--

    public List<webService> get_sessions() {
        return _sessions;
    }

    //------------------

    public userService(String serviceName,databaseService database){
        _sessions = new ArrayList<>();
        _serviceName = serviceName;
        _database = database;
        _apiService = new apiService();
        loggedUsers = new ArrayList<>();
    }

    public void startService(){
        int port = 8080;
        port(port);
        String addr = "http://localhost:"+port;
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
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> {
            String name = req.params(":name");
            shareItem quote = _apiService.getBySymb(name);
            return quote.get_price();
        });
        //top share list

        pathStr = "/"+_serviceName+"/top/:userId";
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> getTop(req.params(":userid")));

        pathStr = "/"+_serviceName+"/userCash/:userId";
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> getUserMoney(req.params(":userid")));

        pathStr = "/"+_serviceName+"/userPurchase/:userId";
        System.out.println(addr+pathStr);
        post(pathStr, (req, res) -> {
            res.type("application/json");
            userItem CurrentUser = getCurrentUser(req.params(":userid"));
            JSONObject bod = new JSONObject(req.body());
            if(CurrentUser == null){return 500;}
            String sym = bod.getString("sym");
            String id = CurrentUser.get_user_id();
            int amount = bod.getInt("amount");
            doPurchase(sym,id,amount);
            return 200;
        });

        pathStr = "/"+_serviceName+"/userSellShare/:userid";
        System.out.println(addr+pathStr);
        post(pathStr, (req, res) -> {
            res.type("application/json");
            userItem CurrentUser = getCurrentUser(req.params(":userid"));
            JSONObject bod = new JSONObject(req.body());

            String sym = bod.getString("sym");
            String id = CurrentUser.get_user_id();
            int amount = bod.getInt("amount");
            doShareSale(sym,id,amount);
            return 200;
        });

        pathStr = "/"+_serviceName+"/userTransactionHistory/:userId";
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> userTransList(req.params(":userid")));

        pathStr = "/"+_serviceName+"/login/:userName/:password";
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> userLogin(req.params(":userName"),req.params(":password")));

        pathStr = "/"+_serviceName+"/regester/:userName/:password";
        System.out.println(addr+pathStr);
        post(pathStr, (req, res) -> userRegester(req.params(":userName"),req.params(":password")));

        pathStr = "/"+_serviceName+"/logout/:userId";
        System.out.println(addr+pathStr);
        get(pathStr, (req, res) -> {
            if(userLogout(req.params(":userid"))){
                return 200;
            }else return 400;
        });
    }

    public databaseService getDbService(){
        return _database;
    }

    public Object getTop(String userId) {
        for(webService x :_sessions){
            if(userId.equals(x.getCurrentUser().get_user_id())){
                try {
                    return x.getTop();
                } catch (IOException e) {
                    e.printStackTrace();
                }}}
        return null;
    }

    public double getUserMoney(String userId) {
        for(webService x :_sessions){
            if(userId.equals(x.getCurrentUser().get_user_id())){
                return x.getCurrentUser().get_Money();
            }
        }
        return -1;
    }

    public boolean doShareSale(String sym, String id, int amount) {
        for(webService x :_sessions){
            if(id.equals(x.getCurrentUser().get_user_id())){
                return x.doShareSale(sym,id,amount);
            }
        }
        return false;
    }

    public JsonArray userTransList(String userId) {
        for(webService x :_sessions){
            if(userId.equals(x.getCurrentUser().get_user_id())){
                return x.userTransList();
            }
        }
        return null;
    }
    public boolean doPurchase(String sym, String id, int amount) {
        for(webService x :_sessions){
            if(id.equals(x.getCurrentUser().get_user_id())){
                return x.doPurchase(sym,id,amount);
            }
        }
        return false;
    }

    public Boolean userLogout(String user_id){
        for(webService x :_sessions){
            if(user_id.equals(x.getCurrentUser().get_user_id())){
                _sessions.remove(x);
                return true;
            }
        }
        return false;
    }

    public String userLogin(String uName, String pWord){
        userItem user = _database.getUserLogin(uName,pWord);
        //user.set_Database(_database);
        //add webservice to sessions
        try {
            _sessions.add(new webService(user.get_user_id(),_database,user));
        } catch (IOException e) {
            e.printStackTrace();
            return " ";
        }
        //
        loggedUsers.add(user);
        return user.get_user_id();
    }

    public String userRegester(String uName, String pWord){
        userItem user;
        if(_database.regesterUser(uName,pWord)){
            user = _database.getUserLogin(uName,pWord);
            return user.get_user_id();
        }else return " ";
    }

    private userItem getCurrentUser(String user_id){
        for (userItem u: loggedUsers) {
            if(user_id.equals(u.get_user_id())){
                return u;
            }
        }
        return null;
    }
    public void stopService(){
        stop();
        _database = null;
    }
}
