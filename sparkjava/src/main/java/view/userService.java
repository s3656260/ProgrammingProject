package view;

import controller.databaseService;
import model.userItem;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
/*TODO
 * Open service endpoint that recieves username, and hash password
 * -check db if login matches
 * -return api session url
 * Open service endpoint for regester recieve uname pword
 * -check if already exists
 * -return success
 */
public class userService {
    public static String DEFULT_LOGIN_API = "loginService";
    private String _serviceName;
    private List<webService> sessions;

    public void startService(){
        sessions = new ArrayList<>();
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
        String pathStr = "/"+_serviceName+"/login/:userName/:password";
        get(pathStr, (req, res) -> userLogin());
        pathStr = "/"+_serviceName+"/Regester/:userId";
        post(pathStr, (req, res) -> userRegester());

    }
    public userService(String serviceName,databaseService database){
        _serviceName = serviceName;
    }

    private userItem userLogin(String uName, String pWord){
        return null;
    }
    private userItem userRegester(String uName, String pWord){
        return null;
    }
}
