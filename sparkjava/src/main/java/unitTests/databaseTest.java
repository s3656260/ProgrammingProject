package unitTests;

import controller.databaseService;
import org.junit.*;

import static controller.databaseService.TEST_DB;

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
        this.dbService.addStockPurchase("1","OHI",3);
    }

}
