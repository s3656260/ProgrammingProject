import org.apache.log4j.BasicConfigurator;
import view.webService;

import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        //apiService foo = new apiService();
        //foo.genList();
        liveService();
    }
    private static void liveService(){
        webService test = new webService("test","foo");
        test.startService();
    }
}
