package view;

import controller.databaseService;
import model.userItem;

import static spark.Spark.*;

public class userService {
    public static String DEFULT_LOGIN_API = "loginService";
    private String _serviceName;
    /*TODO
     * Open service endpoint that recieves username, and hash password
     * -check db if login matches
     * -return api session url
     *
     * Open service endpoint for regester recieve unam pword
     * -check if already exists
     * -return success
     */
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
        String pathStr = "/"+_serviceName+"/userTransactionHistory/:userId";
        get(pathStr, (req, res) -> userLogin());

    }
    public userService(String serviceName,databaseService database){
        _serviceName = serviceName;
    }

    public userItem userLogin(){
        
    }
}
