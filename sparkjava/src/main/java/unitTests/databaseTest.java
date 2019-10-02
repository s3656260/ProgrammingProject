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

        //test insert
        this.dbService.transaction(userId,"OHI",amnt,null,0);
        assertEquals(this.dbService.getAmountUserOwnes(userId,symbol),amnt);

        //test update
        amnt = 2;
        this.dbService.transaction(userId,"OHI",amnt,null,0);
        assertEquals(this.dbService.getAmountUserOwnes(userId,symbol),amnt);

        //test delete
        amnt = 0;
        this.dbService.transaction(userId,"OHI",amnt,null,0);
        assertEquals(this.dbService.getAmountUserOwnes(userId,symbol),amnt);

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
        this.dbService.insertToTransactions(u_id,symbol,1,PURCHASE_TYPE,10);

        //assert list has right size
        lst = this.dbService.getUserTransactionList(u_id);
        assertEquals(1,lst.size());
    }

    @Test
    public void testFullTransProcess(){
        System.out.println("databaseTest.testFullTransProcess");

        //init all tables
        this.dbService.inititialiseTables();

        //set test variables
        List<transaction> lst;
        String u_id = "1",symbol = "OHI";
        int amount = 1;
        double val = 10.01;

        //run transaction with full params
        this.dbService.transaction(u_id,symbol,amount,PURCHASE_TYPE,val);
        lst = this.dbService.getUserTransactionList(u_id);
        //test successul database edit
        assertEquals(this.dbService.getAmountUserOwnes(u_id,symbol),amount);
        assertEquals(lst.size(),1);

    }

}
