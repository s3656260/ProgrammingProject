package view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.apiService;
import model.shareItem;
import model.userItem;
import org.json.JSONObject;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static spark.Spark.*;

public class webService {

    private String _serviceName;
    private String _serviceAction;
    private apiService _apiService;
    private userItem CurrentUser;

    public webService(String serviceName, String serviceAction){
        _serviceAction = serviceAction;
        _serviceName = serviceName;
        _apiService = new apiService();
        CurrentUser = new userItem(10000);
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
            Quote quote = _apiService.getBySymb(name);
            BigDecimal price = quote.getLatestPrice();
            System.out.println(price);
            return price.toString();
        });
        //top share list
        pathStr = "/"+_serviceName+"/top";
        get(pathStr, (req, res) -> getTop());
        pathStr = "/"+_serviceName+"/userCash/:userId";
        get(pathStr, (req, res) -> getUserMoney());
     }
     private JSONObject getUserMoney(){
        double val = CurrentUser.get_Money();
        JSONObject json = new JSONObject();
        json.put("userMoney",val);
        return json;
     }
    private int checkForUserStock(String symbol){
        return 0;
    }
    private Object getTop() throws IOException {
        List<shareItem> allShares = _apiService.genList();
        JsonObject pack = new JsonObject();
        JsonArray list = new JsonArray();
        for (shareItem x :allShares) {
            int i = checkForUserStock(x.getSymbol());

            list.add(x.toJson(i));
        }
        //pack.add("items",list);
        return list;
        //return addUserAmounts(list);
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
