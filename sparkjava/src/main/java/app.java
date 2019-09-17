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
        //test();
    }
    private static void test() throws IOException {
        apiService test = new apiService();
        test.genList();
    }
    private static void liveService(){
        webService test = new webService("test","foo");
        test.startService();
    }

    /*private static void makeJson(){
        jsonService jService = new jsonService("database/companyNames.json");
        String str = jService.getAsString();
        JSONArray jArr = new JSONArray(str);
        // /\ method to get list /\

        JSONObject jOb = new JSONObject();
        for (int i=0; i < jArr.length(); i++) {
            JSONObject x = jArr.getJSONObject(i);
            String sym = x.getString("symbol");
            String cName = x.getString("name");

            jOb.put(sym,cName);
        }
        //String sr = jArr.getJSONObject(1).getString("name");
        String sr = jOb.getString("A");
        System.out.println(sr);

        try (FileWriter file = new FileWriter("database/cNameList.json")) {
            file.write(jOb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
