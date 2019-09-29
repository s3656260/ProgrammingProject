package unitTests;

import model.shareItem;
import model.userItem;
import org.apache.log4j.BasicConfigurator;
import org.junit.*;
import view.webService;

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
        int index = 1;
        //set test variables
        shareItem testShare = web_service.getTestShare(index);
        String symbol = testShare.getSymbol();
        String userId = "1";
        double price = this.web_service.get_apiService().getBySymb(symbol).getLatestPrice().doubleValue();
        int amount = 10;
        userItem testUser = this.web_service.getCurrentUser();

        //test expected variables
        int stockListLength = this.web_service.getStockList().size();
        double expectedUserMoney = (testUser.get_Money())-(amount*price);
        //run test purchase
        this.web_service.testPurchase(symbol,userId,amount);
        //values after purchase
        int stockCount = this.web_service.stockCount(index);
        //run assertions
        //changes to happen
        Assert.assertEquals("User Money is not as expected after purchase ",expectedUserMoney,this.web_service.getCurrentUser().get_Money(),0);
        Assert.assertEquals("Stock incremented incorrectly",amount,stockCount,0);
        //not to change
        Assert.assertEquals("Stock list size changed unexpectedly",stockListLength,this.web_service.getStockList().size(),0);
    }
}
