package view;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import controller.apiService;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;

import static spark.Spark.*;

public class webService {

    private String _serviceName;
    private String _serviceAction;
    private apiService _apiService;

    public webService(String serviceName, String serviceAction){
        _serviceAction = serviceAction;
        _serviceName = serviceName;
        _apiService = new apiService();
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
        String pathStr = "/"+_serviceName+"/sym/:name";
        get(pathStr, (req, res) -> {
            String name = req.params(":name");
            Quote quote = _apiService.getBySymb(name);
            BigDecimal price = quote.getLatestPrice();
            System.out.println(price);
            return price.toString();
        });
        pathStr = "/"+_serviceName+"/top";
        get(pathStr, (req, res) -> getTop());
        //post(pathStr, (request, response) ->{});
     }

    public Object getTop(){
        /*List<shareItem> allShares = _apiService.genList();
        JsonObject pack = new JsonObject();
        JsonArray list = new JsonArray();
        for (shareItem x :allShares) {
            //x.updateStock(_apiService);
            list.add(x.toJson());
        }
        pack.add("items",list);*/
        return testJava();
    }
    public Object testJava(){
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
        return arrayFromString;
    }
}
