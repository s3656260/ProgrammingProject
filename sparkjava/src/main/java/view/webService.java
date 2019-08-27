package view;

import com.google.gson.JsonArray;
import controller.apiService;
import model.shareItem;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.util.List;

import static spark.Spark.get;
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
        String pathStr = "/"+_serviceName+"/sym/:name";
        get(pathStr, (req, res) -> {
            String name = req.params(":name");
            Quote quote = _apiService.getBySymb(name);
            BigDecimal price = quote.getLatestPrice();
            System.out.println(price);
            return price.toString();
        });
        pathStr = "/"+_serviceName+"/top";
        get(pathStr, (req, res) -> {
            return (JsonArray) getTop();
        });
        //post(pathStr, (request, response) ->{});
     }

    public Object getTop(){
        //popularShares popular = new popularShares();
        //List<shareItem> popShares = popular.getList();
        //List<shareItem> allShares = _apiService.getSymbols();
        List<shareItem> allShares = _apiService.genList();
        JsonArray list = new JsonArray();
        for (shareItem x :allShares) {
            //x.updateStock(_apiService);
            list.add(x.toJson());
        }
        return list;
    }
}
