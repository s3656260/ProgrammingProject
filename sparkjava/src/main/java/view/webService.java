package view;
import static spark.Spark.*;
public class webService {

    private String _serviceName;
    private String _serviceAction;

    public webService(String serviceName, String serviceAction){
        _serviceAction = serviceAction;
        _serviceName = serviceName;
    }

    public void startService(){
        String pathStr = "/"+_serviceName;
        get(pathStr, (req, res) -> _serviceAction);
        //post(pathStr, (request, response) ->{});
     }
}
