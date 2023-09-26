package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.Executors;


@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public class Broker {

    static LinkedList<String> url_lists = new LinkedList<String>();
    String host = "localhost";
    int port = 9000;

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {
        LinkedList<Quotation> quotations = new LinkedList<Quotation>();

        try {

            for (String urllist : url_lists) {
                URL wsdlUrl = new URL(urllist);
                QName serviceName = new QName("http://core.service/", "QuoterService");
                Service service = Service.create(wsdlUrl, serviceName);
                QName portName = new QName("http://core.service/", "QuoterPort");
                QuoterService quotationService = service.getPort(portName, QuoterService.class);
                quotations.add(quotationService.generateQuotation(info));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return quotations;

    }

    public static void main(String[] args) {
        try {
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();

            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            jmdns.addServiceListener("_quote._tcp.local.", new WSDLServiceListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class WSDLServiceListener implements ServiceListener {


        @Override
        public void serviceAdded(ServiceEvent serviceEvent) {

        }

        @Override
        public void serviceRemoved(ServiceEvent serviceEvent) {

        }

        @Override
        public void serviceResolved(ServiceEvent serviceEvent) {
            String servicepath = serviceEvent.getInfo().getPropertyString("path");
            if (servicepath != null) {
                String url = serviceEvent.getInfo().getURLs()[0];
                System.out.println("URL : " + url);

                if (!url_lists.contains(url)) { url_lists.add(url);
                }
            }
        }
    }
}