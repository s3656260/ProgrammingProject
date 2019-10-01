package unitTests;

import controller.databaseService;
import org.junit.*;

import static controller.databaseService.TEST_DB;
import static org.testng.Assert.*;

public class databaseTest {

    databaseService dbService;

    @BeforeClass
    public static void initExpensiveResource(){System.out.println("databaseTest.init");}

    @AfterClass
    public static void destroyExpensiveResource(){System.out.println("databaseTest.destroy");}

    @Before
    public void setUp() throws Exception{
        System.out.println("databaseTest.setUp");
        //setup database conn
        dbService = new databaseService(TEST_DB);
    }

    @After
    public void tearDown() throws Exception{
        System.out.println("databaseTest.tearDown");
        //remove connection
        dbService.deleteDatabase();
        dbService = null;
    }

    @Test
    public void testOwnededTable(){
        System.out.println("databaseTest.testOwnededTable");
        this.dbService.mkOwnedStockTable();
        int amnt = 1;
        String userId = "1", symbol = "OHI";

        this.dbService.addStockPurchase(userId,"OHI",amnt);
        this.dbService.addStockPurchase(userId,"OHI",amnt);

        assertEquals(this.dbService.getAmountUserOwnes(userId,symbol),amnt+amnt);
    }

}
