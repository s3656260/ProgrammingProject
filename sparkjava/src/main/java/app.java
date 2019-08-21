import org.apache.log4j.BasicConfigurator;
import view.webService;

import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        //String foo = getApple();
        //System.out.println(foo);
        webService test = new webService("test","foo");
        test.startService();
    }
}
