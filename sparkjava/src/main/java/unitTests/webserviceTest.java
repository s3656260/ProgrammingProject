package unitTests;

import model.shareItem;
import model.userItem;
import org.junit.*;
import view.webService;

import static org.junit.Assert.*;

public class webserviceTest {
    private webService web_service;
    //these two strings dont make a whole much of a difference unless using the service endpoint
    private final String service_name = "test";
    private final String service_func = "test";

    @BeforeClass
    public static void initExpensiveResource(){System.out.println("webserviceTest.init");}

    @AfterClass
    public static void destroyExpensiveResource(){System.out.println("webserviceTest.destroy");}

    @Before
    public void setUp() throws Exception{
        System.out.println("webserviceTest.setUp");
        //setup webservice
        this.web_service = new webService(service_name,service_func);
        this.web_service.startService();
    }

    @After
    public void tearDown() throws Exception{
        System.out.println("webserviceTest.tearDown");
        //remove webservice
        this.web_service.stopService();
        this.web_service = null;
    }

    @Test
    public void purchaseTest(){
        System.out.println("webserviceTest.purchaseTest");
        //set test variables
        shareItem testShare = web_service.getTestShare(1);
        String symbol = testShare.getSymbol();
        String userId = "1";
        int amount = 10;
        userItem testUser = this.web_service.getCurrentUser();
        //test expected variables
        //double expectedCurrency = (testUser.get_Money()*);
        //run test purchase
        this.web_service.testPurchase(symbol,userId,amount);
        //run assertions
        //changes to happen
            //user has less money
        assert
            //user ownes more stock
        //not to change
            //amount of stocks in list
            //
    }
}
