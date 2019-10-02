package unitTests;

import controller.databaseService;
import model.transaction;
import org.json.JSONObject;
import org.junit.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static controller.databaseService.*;
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
        dbService.destroyTables();
        dbService.close();
        dbService.deleteDatabase();
        dbService = null;
    }

    @Test
    public void testOwnedTable(){
        System.out.println("databaseTest.testOwnedTable");
        this.dbService.mkOwnedStockTable();
        //assert table is created
        assertTrue(this.dbService.checkTableExists(OWNED_STOCK_TABLE));

        int amnt = 1;
        String userId = "1", symbol = "OHI";

        this.dbService.transaction(userId,"OHI",amnt,null);
        this.dbService.transaction(userId,"OHI",amnt,null);

        assertEquals(this.dbService.getAmountUserOwnes(userId,symbol),amnt);

        this.dbService.transaction(userId,"OHI",0,null);
    }

    @Test
    public void testTransactionTable(){
        System.out.println("databaseTest.testTransactionTable");

        //test table creation
        this.dbService.mkTransactionTable();
        assertTrue(this.dbService.checkTableExists(TRANSACTION_TABLE));

        //set test variables
        List<transaction> lst;
        String u_id = "1",symbol = "OHI";

        //test transaction function standalone
        this.dbService.insertToTransactions(u_id,symbol,1,PURCHASE_TYPE);

        //assert list has right size
        lst = this.dbService.getUserTransactionList(u_id);
        assertEquals(1,lst.size());

        //test transactions as part of process
        this.dbService.transaction(u_id,symbol,1,PURCHASE_TYPE);
        lst =this.dbService.getUserTransactionList(u_id);
        assertEquals(2,lst.size());
    }

}
