import org.junit.BeforeClass;
import org.junit.Test;
import service.auldfellas.AFQService;
import service.broker.LocalBrokerService;
import service.core.*;
import service.dodgydrivers.DDQService;
import service.girlpower.GPQService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrokerUnitTest {
    private static final ClientInfo CLIENT_INFO = new ClientInfo("Gunjan", ClientInfo.MALE, 30, 30, 1, "ABC123/7");
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        LocalBrokerService localBrokerService = new LocalBrokerService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(localBrokerService, 0);
            registry.bind(Constants.BROKER_SERVICE, brokerService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void getQuotationsNotNullTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void getQuotationsEmptyTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        List<Quotation> quotations = service.getQuotations(CLIENT_INFO);
        assertEquals(0, quotations.size());
    }

    @Test
    public void getQuotationsNotEmptyTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

        // Bind AFQService
        QuotationService afqService = new AFQService();
        QuotationService quotationService1 = (QuotationService) UnicastRemoteObject.exportObject(afqService, 0);
        registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService1);

        // Bind DDQService
        QuotationService ddqService = new DDQService();
        QuotationService quotationService2 = (QuotationService) UnicastRemoteObject.exportObject(ddqService, 0);
        registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService2);

        // Bind GPQService
        QuotationService gpqService = new GPQService();
        QuotationService quotationService3 = (QuotationService) UnicastRemoteObject.exportObject(gpqService, 0);
        registry.bind(Constants.GIRL_POWER_SERVICE, quotationService3);

        // Generate quotations for all services
        List<Quotation> quotations = service.getQuotations(CLIENT_INFO);

        System.out.println(CLIENT_INFO);
        for (Quotation q: quotations) {
            System.out.println(q);
        }

        assertEquals(3, quotations.size());
    }


}