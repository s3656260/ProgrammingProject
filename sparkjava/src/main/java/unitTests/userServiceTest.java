package unitTests;

import com.google.gson.JsonArray;
import controller.databaseService;
import model.shareItem;
import model.userItem;
import org.json.JSONObject;
import org.junit.*;
import view.userService;
import view.webService;

import java.util.List;

import static controller.databaseService.TEST_DB;
import static org.junit.Assert.*;
import static spark.Spark.*;

public class userServiceTest {
    userService UserService;
    private final String service_name = "test";
    private final String service_func = "test";
    private final String test_user = "tUser";
    private final String test_pass = "tPass";

    @BeforeClass
    public static void initExpensiveResource() {
        System.out.println("userServiceTest.init");
    }

    @AfterClass
    public static void destroyExpensiveResource() {
        System.out.println("userServiceTest.destroy");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("userServiceTest.setUp");
        //setup webservice
        databaseService db = new databaseService(TEST_DB);
        db.inititialiseTables();
        this.UserService = new userService(service_name, db);
        this.UserService.userRegester(test_user,test_pass);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("userServiceTest.tearDown");
        //remove webservice
        this.UserService.getDbService().destroyTables();
        this.UserService.stopService();
        this.UserService = null;
    }

    @Test
    public void getTopTest() {
        System.out.println("userServiceTest.getTopTest");

        //login to test account
        String id = this.UserService.userLogin(test_user,test_pass);

        //get test list
        JsonArray test = (JsonArray) this.UserService.getTop(id);
        assertNotNull(test);
    }

    @Test
    public void getUserMoneyTest() {
        System.out.println("userServiceTest.getUserMoneyTest");

        //login to test account
        String id = this.UserService.userLogin(test_user,test_pass);

        //get test balance
        assertNotEquals(-1,this.UserService.getUserMoney(id));
    }

    @Test
    public void doShareSale() {
        System.out.println("userServiceTest.doShareSale");

        //login to test account
        String id = this.UserService.userLogin(test_user,test_pass);

        //test vars
        String symbol = "OHI";
        int amount = 1;

        //Run assertion
        assertTrue(this.UserService.doPurchase(symbol,id,amount));
        assertTrue(this.UserService.doShareSale(symbol,id,amount));
    }

    @Test
    public void userTransList() {
        System.out.println("userServiceTest.userTransList");

        //login to test account
        String id = this.UserService.userLogin(test_user,test_pass);

        //test vars
        String symbol = "OHI";
        int amount = 1;

        //Run assertion
        assertTrue(this.UserService.doPurchase(symbol,id,amount));
        assertTrue(this.UserService.doShareSale(symbol,id,amount));
        JsonArray arr = this.UserService.userTransList(id);
        assertNotNull(arr);
        assertEquals(2,arr.size());
    }

    @Test
    public void doPurchase() {
        System.out.println("userServiceTest.doPurchase");

        //login to test account
        String id = this.UserService.userLogin(test_user,test_pass);

        //test vars
        String symbol = "OHI";
        int amount = 1;

        //Run assertion
        assertTrue(this.UserService.doPurchase(symbol,id,amount));

    }


    @Test
    public void userLogin(){
        System.out.println("userServiceTest.userLogin");
        //test putting user in works
        String res = this.UserService.userLogin(test_user,test_pass);

        assertNotEquals(" ",res);

    }

    @Test
    public void userLogout(){
        System.out.println("userServiceTest.userLogout");

        //put in test user
        String id = this.UserService.userLogin(test_user,test_pass);

        //get length before
        List<webService> loggedUsers = this.UserService.get_sessions();
        assertEquals(1,loggedUsers.size());
        // do logout
        assertTrue(this.UserService.userLogout(id));
        //test size decreaseed
        assertEquals(0,loggedUsers.size());
    }

    @Test
    public void userRegester(){
        System.out.println("userServiceTest.userRegester");

        //setup test variables
        String uname="user", pword="pass";

        //test putting user in works
        String res = this.UserService.userRegester(uname,pword);
        System.out.println(res);
        assertNotEquals(" ",res);

    }
}
