import controller.apiService;
import controller.databaseService;
import controller.jsonService;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import view.webService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static controller.databaseService.DEFAULT_DB;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        liveService();
        test();
    }
    private static void test() throws IOException {

    }
    private static void liveService() throws IOException {
        databaseService db = new databaseService(DEFAULT_DB);
        webService test = new webService("test","foo",db);
        test.startService();
    }
}
