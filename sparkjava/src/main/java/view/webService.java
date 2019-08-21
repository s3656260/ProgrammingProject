package view;

import com.google.gson.JsonArray;
import controller.apiService;
import model.popularShares;
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
            popularShares popular = new popularShares();
            List<shareItem> popShares = popular.getList();
            JsonArray list = new JsonArray();
            for (shareItem x :popShares) {
                list.add(x.toJson());
            }
            return list;
        });
        //post(pathStr, (request, response) ->{});
     }
}
