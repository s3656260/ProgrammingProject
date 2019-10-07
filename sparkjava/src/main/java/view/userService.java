package view;

import model.userItem;

import static spark.Spark.before;
import static spark.Spark.options;

public class userService {
    public static String DEFULT_LOGIN_API = "loginService";
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


    }
    public userService(String "serviceName"){

    }

    public void startDatabase(){

    }

    public userItem userLogin(){

    }
}
