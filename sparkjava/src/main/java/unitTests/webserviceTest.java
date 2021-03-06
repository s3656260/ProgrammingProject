package unitTests;

import com.google.gson.JsonArray;
import controller.apiService;
import controller.databaseService;
import model.shareItem;
import model.transaction;
import model.userItem;
import org.junit.*;
import view.webService;

import java.io.IOException;
import java.util.List;

import static controller.databaseService.TEST_DB;
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
        databaseService db = new databaseService(TEST_DB);
        db.inititialiseTables();
        this.web_service = new webService(service_name,service_func,db);
    }

    @After
    public void tearDown() throws Exception{
        System.out.println("webserviceTest.tearDown");
        //remove webservice
        this.web_service.getDBservice().destroyTables();
        this.web_service = null;
    }

    @Test
    public void shareListGenerateTest(){
        System.out.println("webserviceTest.shareListGenerateTest");
        //setup test variable
        int index = 1;
        //reset list for testing
        this.web_service.resetList();
        //test list is empty
        assertNull(this.web_service.getList());
        //generate list
        try {
            this.web_service.testGenList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //test list is no longer null
        assertNotNull(this.web_service.getList());
        //test list size is greater than 0
        assertNotEquals(0,this.web_service.getList().size());
        //test the stock list has values
        assertNotNull(this.web_service.getTestListStock(index));
        //
    }

    @Test
    public void sellShareTest(){
        System.out.println("webserviceTest.sellShareTest");
        int ownedAmnt = 2;
        int index = 1;

        // add a test stock ownership
        this.web_service.testAddStockOwnership(index,ownedAmnt);
        JsonArray res = this.web_service.getStocksOwned();

        //set test variables
        shareItem testShare = web_service.getTestShare(index);
        String symbol = testShare.getSymbol();
        String userId = "1";
        double price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
        int amount = 1;
        userItem testUser = this.web_service.getCurrentUser();

        //test expected variables
        int stockListLength = this.web_service.getStockList().size();
        double expectedEarn = testUser.get_Money()+(price*amount);

        //run a test purchase
        boolean saleRes = this.web_service.testSale(symbol,userId,amount);

        //assert sale happened
        assertTrue("couldnt complete test sale",saleRes);

        //assert user has earnt amount equal to price time amount
        assertEquals(expectedEarn,this.web_service.getCurrentUser().get_Money(),0);

        //not to change
        assertEquals("Stock list size changed unexpectedly",stockListLength,this.web_service.getStockList().size(),0);


    }

    @Test
    public void invalidSellShareTest(){
        System.out.println("webserviceTest.invalidSellShareTest");
    }

    @Test
    public void purchaseTest(){
        System.out.println("webserviceTest.purchaseTest");
        int index = 1;

        //set test variables
        shareItem testShare = web_service.getTestShare(index);
        String symbol = testShare.getSymbol();
        String userId = "1";
        double price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
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
        assertEquals("User Money is not as expected after purchase ",expectedUserMoney,this.web_service.getCurrentUser().get_Money(),0);
        assertEquals("Stock incremented incorrectly",amount,stockCount,0);

        //not to change
        assertEquals("Stock list size changed unexpectedly",stockListLength,this.web_service.getStockList().size(),0);

        //reset user currency
        this.web_service.setUserMoney(10000);
    }

    @Test
    public void invalidPurchaseTest(){
        System.out.println("webserviceTest.invalidPurchaseTest");
        int index = 1;

        //set test variables
        shareItem testShare = web_service.getTestShare(index);
        String symbol = testShare.getSymbol();
        String userId = "1";
        double price;
        int amount = 1;

        //no money test
        price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
        this.web_service.setUserMoney(0);
        assertFalse(this.web_service.testPurchase(symbol,userId,amount));

        //price-1 money test just not enough
        price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
        this.web_service.setUserMoney(price-1);
        assertFalse(this.web_service.testPurchase(symbol,userId,amount));

        //just enough price
        price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
        this.web_service.setUserMoney(price);
        assertTrue(this.web_service.testPurchase(symbol, userId, amount));

        //reset user currency
        this.web_service.setUserMoney(10000);
    }

    @Test
    public void getPriceTest() {
        System.out.println("webserviceTest.getPriceTest");

        //set test variables
        int index = 1;

        //get test stock
        shareItem testShare = web_service.getTestShare(index);

        //start up api to test price
        apiService testApi = this.web_service.get_apiService();

        //assert price
        shareItem expectedShare = testApi.getBySymb(testShare.getSymbol());
        assertEquals(testShare.get_price(),expectedShare.get_price());
    }

    @Test
    public void databaseTest() {
        System.out.println("webserviceTest.databaseTest");
        int index = 1;

        //set test variables
        shareItem testShare = web_service.getTestShare(index);
        String symbol = testShare.getSymbol(), userId = "1";
        double price;
        int amount = 1;

        //test purchase
        price = Double.parseDouble(this.web_service.get_apiService().getBySymb(symbol).get_price());
        this.web_service.setUserMoney(1000);

        //assert purchase
        assertTrue("couldn't complete purchase",this.web_service.testPurchase(symbol,userId,amount));

        //assert variable in db is correct
        assertEquals(amount,this.web_service.getDBservice().getAmountUserOwnes(userId,symbol));

        //assert sale
        assertTrue("couldn't complete purchase",this.web_service.testSale(symbol,userId,amount));

        //assert variable in db is correct / now should return 0 as to better report stocks as values
        assertEquals(0,this.web_service.getDBservice().getAmountUserOwnes(userId,symbol));
    }

    @Test
    public void transactionTest(){
        System.out.println("webserviceTest.transactionTest");

        //setup test vars
        String symbol ="OHI", id = "1";
        int amount = 1;

        //run test transactions
        assertTrue(this.web_service.testPurchase(symbol, id, amount));
        assertTrue(this.web_service.testSale(symbol, id, amount));

        //get test list
        List<transaction> res = this.web_service.testTransactionList();
        assertEquals(2,res.size());
    }

    @Test
    public void shareOwnership(){
        System.out.println("webserviceTest.shareOwnership");

        //setup test vars
        String symbol ="OHI", id = "1";
        int amount = 1;
        int amountSec = 10;

        //run test transactions
        assertTrue(this.web_service.testPurchase(symbol, id, amount));
        assertTrue(this.web_service.testPurchase(symbol, id, amountSec));
        assertTrue(this.web_service.testSale(symbol, id, amount));

        //get test list
        List<transaction> res = this.web_service.testTransactionList();
        assertEquals(3,res.size());
        transaction testTrans = res.get(1);
        assertEquals(amountSec,testTrans.get_Amount());
    }
}
