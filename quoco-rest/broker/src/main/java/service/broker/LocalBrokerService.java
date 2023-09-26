package service.broker;


import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */

@RestController
public class LocalBrokerService {                                           //4b


    static String[] services = {

            "http://localhost:8081/quotations", //auldfellas
            "http://localhost:8082/quotations", //dodgydrivers
            "http://localhost:8083/quotations"  // girlpower

    };
    HashMap<Integer, ClientApplication> clientApplications = new HashMap<Integer, ClientApplication>() {
    };
    int appNumber = 0;

    @RequestMapping(value = "/application", method = RequestMethod.POST)
    public ClientApplication getApplication(@RequestBody ClientInfo info) {
        ClientApplication clientApplication = new ClientApplication();
        System.out.println(info);
        clientApplication.setQuotationList(getQuotations(info));
        clientApplication.setClientInfo(info);
        clientApplication.setApplicationNumber(appNumber);
        System.out.println(appNumber);
        clientApplications.put(appNumber++, clientApplication);
        return clientApplication;
    }

    public List<Quotation> getQuotations(ClientInfo info) {
        List<Quotation> quotations = new LinkedList<Quotation>();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ClientInfo> requests = new HttpEntity<>(info);
        for (String service : services) {
            try {
                Quotation quotation = restTemplate.postForObject(service, requests, Quotation.class);
                quotations.add(quotation);
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
        return quotations;
    }

    //4c
    @RequestMapping(value = "/application/{application-number}", method = RequestMethod.GET)
    public ClientApplication searchApplication(@PathVariable("application-number") int parameter) {
        return clientApplications.get(parameter);
    }

    //4d
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public List<ClientApplication> listApplication() {
        return new ArrayList<ClientApplication>(clientApplications.values());
    }
}
