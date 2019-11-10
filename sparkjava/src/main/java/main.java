import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        //BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        app.liveService();
        //testService();
    }
}
