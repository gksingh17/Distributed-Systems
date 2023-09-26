import org.junit.BeforeClass;
import org.junit.Test;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;
import service.core.QuotationService;
import service.dodgydrivers.DDQService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DodgydriversUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        DDQService ddqService = new DDQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(ddqService, 0);
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        ClientInfo client = new ClientInfo("Gunjan", ClientInfo.MALE, 30, 30, 1, "ABC123/7");
        Quotation quotation = service.generateQuotation(client);
        assertTrue(quotation instanceof Quotation);
        assertNotNull(quotation);
    }
}