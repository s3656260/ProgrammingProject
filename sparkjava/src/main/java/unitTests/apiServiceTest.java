package unitTests;

import controller.apiService;
import org.junit.*;

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
        //test url
        assertTrue(this.api_service.tryUrl());
        //test connection returns true
        assertTrue(this.api_service.tryConnect());
    }

}
