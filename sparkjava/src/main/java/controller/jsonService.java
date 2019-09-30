package controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class jsonService {
    private  String fp = null;
    public jsonService(String filePath){
        fp = filePath;
    }

    public String getAsString(){
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fp)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public Object getArray() throws IOException {
        FileReader f = new FileReader(fp);
        BufferedReader br = new BufferedReader(f);

        String currentJSONString  = "";
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<JSONObject>();

        while( (currentJSONString = br.readLine()) != null ) {
            JSONObject currentObject = new JSONObject(currentJSONString);
            jsonObjectArray.add(currentObject);
            }
        return jsonObjectArray;
    }
}
