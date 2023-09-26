package service.actor;

import akka.actor.*;
import scala.concurrent.duration.Duration;
import service.auldfellas.AFQService;
import service.message.Init;
import service.messages.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Broker extends AbstractActor {
    private static int SEED_ID = 1;  //identifier
    ActorSystem actorsystem = ActorSystem.create(); //new actor
    List<ActorRef> actorReflist = new LinkedList<>();   //list for available quotations services
    HashMap<Integer, ApplicationResponse> requestMap = new HashMap<Integer, ApplicationResponse>(); //map identifier to the client info and quotations
    ActorRef client = new ActorRef() {

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public ActorPath path() {
            return null;
        }
    };

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder() //for handling client requests for quotations
                .match(ApplicationRequest.class, msg -> {
                    client = getSender();
                    requestMap.put(SEED_ID, new ApplicationResponse(msg.getClientInfo()));

                    for (ActorRef ref : actorReflist) {
                        ref.tell(new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf());
                    }
                    getContext().system().scheduler().scheduleOnce(Duration.create(3, TimeUnit.SECONDS), getSelf(), new RequestDeadline(SEED_ID++), getContext().dispatcher(), null);
                }).match(String.class,   //for handling quotation services messages for registration
                        msg -> {
                            if (!msg.equals("Register")) return;
                            actorReflist.add(getSender());
                            System.out.println("Registered " + getSender().toString() + " Successfully :" + actorReflist.size());
                        }).match(QuotationResponse.class, msg -> {
                    requestMap.get(msg.getId()).addQuotations(msg.getQuotation());
                }).match(RequestDeadline.class, msg -> {
                    client.tell(requestMap.get(msg.getSEED_ID()), null);
                }).build();
    }

    public ActorRef CreateRef(String userName) {
        ActorRef ref = actorsystem.actorOf(Props.create(Quoter.class), userName); //Actor Reference
        ref.tell(new Init(new AFQService()), null);
        ActorSelection selection = actorsystem.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker"); //Actor call
        selection.tell("register", ref);
        return ref;
    }
}