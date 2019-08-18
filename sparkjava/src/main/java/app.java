import controller.apiService;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import view.webService;

import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        String foo = getApple();
        //System.out.println(foo);
        webService test = new webService("test",foo);
        test.startService();
    }
    public static String getApple() throws IOException {
        apiService apple = new apiService();
        String foo = apple.doGet("foo");
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(foo);
    }
}
