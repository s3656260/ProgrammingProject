import org.apache.log4j.BasicConfigurator;
import view.webService;

public class app {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        webService test = new webService("test",app::sayHello);
        test.startService();
    }
    public static String sayHello(){
        return "hello";
    }
}
