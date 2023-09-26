import service.broker.LocalBrokerService;
import service.core.BrokerService;
import service.core.Constants;
import service.core.QuotationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        BrokerService brokerService = new LocalBrokerService();
        try {
            // Connect to the RMI Registry - creating the registry will be the// responsibility of the broker.

            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                String host = args[0];
                registry = LocateRegistry.getRegistry(host, 1099);
            }

            // Create the Remote Object
            BrokerService quotationService = (BrokerService) UnicastRemoteObject.exportObject(brokerService, 0);

            // Register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, quotationService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}