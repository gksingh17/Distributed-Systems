package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
public class LocalBrokerService implements BrokerService {

    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry();
        List<Quotation> quotations = new LinkedList<Quotation>();

        for (String name : registry.list()) {
            if (name.startsWith("qs-")) {
                QuotationService service = null;
                try {
                    service = (QuotationService) registry.lookup(name);
                    quotations.add(service.generateQuotation(info));
                } catch (NotBoundException e) {
                    System.out.println("Could not locate Quotation Service with name `" + name + "`");
                    System.out.println(e.getMessage());
                }
            }
        }

        return quotations;
    }
}
