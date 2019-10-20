import controller.apiService;
import controller.databaseService;
import controller.jsonService;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import view.userService;
import view.webService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static controller.databaseService.DEFAULT_DB;
import static controller.databaseService.TEST_DB;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        liveService();
        //testService();
    }
    private static void testService() throws IOException {
        databaseService db = new databaseService(TEST_DB);
        db.inititialiseTables();
        userService test = new userService("test",db);
        test.startService();
    }
    private static void liveService() throws IOException {
        databaseService db = new databaseService(DEFAULT_DB);
        //db.inititialiseTables();
        userService test = new userService("test",db);
        test.startService();
    }
}
