import controller.apiService;
import controller.jsonService;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import view.webService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        liveService();
        test();
    }
    private static void test() throws IOException {

    }
    private static void liveService() throws IOException {
        webService test = new webService("test","foo");
        test.startService();
    }
}
