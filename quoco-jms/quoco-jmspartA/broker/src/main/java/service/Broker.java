package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.*;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Broker {

    static String host;
    static Map<Long, ClientInfo> cache = new HashMap<>();
    private static Connection connection;


    public static void main(String[] args) {
        host = "localhost";

        if (args.length>0){
            host=args[0];

        }
        try{
            System.out.println("Broker staring on host : " + host);
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
            connection = factory.createConnection();
            connection.setClientID("broker");
            ClientBrokerServiceThread clientbrokerservice = new ClientBrokerServiceThread();
            new Thread(clientbrokerservice).start();
            ServiceBrokerClientThread servicebrokerclient = new ServiceBrokerClientThread();
            new Thread(servicebrokerclient).start();

        }catch (JMSException e) {
            System.out.println(e);
        }
    }

    public static class ClientBrokerServiceThread implements Runnable{

        @Override
        public void run(){
            try{ //4a
                Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                Queue queue = session.createQueue("REQUESTS");
                Topic topic = session.createTopic("APPLICATIONS");
                MessageProducer producer = session.createProducer(topic);
                MessageConsumer consumer = session.createConsumer(queue);
                connection.start();
                while(true){
                    Message message = consumer.receive();
                    if (message instanceof ObjectMessage){
                        Object content = ((ObjectMessage)message).getObject();
                        if (content instanceof QuotationRequestMessage){
                            QuotationRequestMessage request = (QuotationRequestMessage)content;
                            producer.send(message);
                            cache.put(request.id,request.info);
                        }
                    }else{
                        System.out.println("Unkown message:" + message.getClass().getCanonicalName());
                    }
                }
            } catch (JMSException exthread1){
                System.out.println(exthread1);
            }
        }
    }

    public  static class ServiceBrokerClientThread implements  Runnable{

        @Override
        public void run() {
            try{//4b
                Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                Queue queue = session.createQueue("QUOTATIONS");
                Topic topic = session.createTopic("RESPONSE");
                MessageProducer producer = session.createProducer(topic);
                MessageConsumer consumer = session.createConsumer(queue);
                MessageProducer producer2 = session.createProducer(queue);
                connection.start();
                while (true){
                    long id = 0;
                    ArrayList<Quotation> quotationArrayList = new ArrayList<>();
                    while (true){
                        Message message = consumer.receive();
                        if ((message instanceof  ObjectMessage)){
                            Object content = ((ObjectMessage) message).getObject();
                            if (content instanceof QuotationResponseMessage){
                                QuotationResponseMessage request = (QuotationResponseMessage) content;
                                System.out.println(request.id);

                            }

                        }else {
                            System.out.println(quotationArrayList);
                            Message application = session.createObjectMessage(new ClientApplicationMessage(id, cache.get(id), quotationArrayList));
                            producer.send(application);
                        }
                    }
            }

        } catch (JMSException exthread2){
                System.out.println(exthread2);
            }

        }

    }

}


