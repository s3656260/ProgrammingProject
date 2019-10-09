package unitTests;

import controller.databaseService;
import model.shareItem;
import model.userItem;
import org.json.JSONObject;
import org.junit.*;
import view.userService;
import view.webService;

import static controller.databaseService.TEST_DB;
import static org.junit.Assert.*;
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
        System.out.println("userServiceTest.tearDown");

    }

     */

    @Test
    public void userLogin(){
        System.out.println("userServiceTest.userLogin");

        //setup test variables
        String uname="user", pword="pass";
        this.UserService.userRegester(uname,pword);

        //test putting user in works
        String res = this.UserService.userLogin(uname,pword);
        System.out.println(res);
        assertNotEquals(" ",res);

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

    @Test
    public void getCurrentUser(){
        System.out.println("userServiceTest.getCurrentUser");
        //

    }
}
