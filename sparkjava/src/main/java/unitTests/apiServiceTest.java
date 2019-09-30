package unitTests;

import controller.apiService;
import model.shareItem;
import org.junit.*;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class apiServiceTest {
    private apiService api_service;
    @BeforeClass
    public static void initExpensiveResource(){System.out.println("webserviceTest.init");}

    @AfterClass
    public static void destroyExpensiveResource(){System.out.println("webserviceTest.destroy");}

    @Before
    public void setUp() throws Exception{
        System.out.println("webserviceTest.setUp");
        //setup apiService
        api_service = new apiService();
    }

    @After
    public void tearDown() throws Exception{
        System.out.println("webserviceTest.tearDown");
        //remove apiservice
        api_service = null;
    }

    @Test
    public void connectionTest(){
        System.out.println("webserviceTest.connectionTest");
        //test url
        assertTrue(this.api_service.tryUrl());
        //test connection returns true
        assertTrue(this.api_service.tryConnect());
    }
    @Test
    public void connectionReturnTest(){
        System.out.println("webserviceTest.connectionReturnTest");
        //test list generation
        List<shareItem> testList = this.api_service.genList();
        //test return is not null
        assertNotNull(testList);
        //test list contains values
        assertNotEquals(0,testList.size());
    }
    @Test
    public void testIndividualStockConsistancy(){
        System.out.println("webserviceTest.testIndividualStockConsistancy");
        //test case variables
        int index = 1;
        //test list generation
        List<shareItem> testList = this.api_service.genList();
        //get test stock from list
        shareItem testShare = testList.get(index);
        //test share is not null
        assertNotNull(testShare);
        //test individual stock getter
        Quote indStockTest = this.api_service.getBySymb(testShare.getSymbol());
        //test share is not null
        assertNotNull(indStockTest);
        //test values are the same
        //symbol
        assertEquals(testShare.getSymbol(),indStockTest.getSymbol());
        //price
        double expected = Double.parseDouble(testShare.get_price());
        double actual = indStockTest.getLatestPrice().doubleValue();
        //allow a degree of error, of 0.1
        assertEquals(expected,actual,0.1);
    }

}
