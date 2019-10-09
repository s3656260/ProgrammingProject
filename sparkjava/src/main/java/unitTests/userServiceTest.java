package unitTests;

import controller.databaseService;
import model.shareItem;
import model.userItem;
import org.json.JSONObject;
import org.junit.*;
import view.userService;
import view.webService;

import static controller.databaseService.TEST_DB;
import static spark.Spark.*;

public class userServiceTest {
    userService UserService;
    private final String service_name = "test";
    private final String service_func = "test";

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
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("userServiceTest.tearDown");
        //remove webservice
        this.UserService.getDbService().destroyTables();
        this.UserService.stopService();
        this.UserService = null;
    }

    /*

    @Test
    public void getTopTest() {

    }

    @Test
    public void getUserMoneyTest() {

    }

    @Test
    public void doShareSale() {

    }

    @Test
    public void userTransList() {

    }

    @Test
    public void doPurchase() {

    }

     */

    @Test
    public void userLogin(){

    }
    
    public void userRegester(){

    }

    @Test
    public void getCurrentUser(){

    }
}
