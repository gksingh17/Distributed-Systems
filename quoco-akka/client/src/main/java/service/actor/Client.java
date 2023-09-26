package service.actor;

import akka.actor.AbstractActor;
import service.Main;
import service.messages.ApplicationResponse;

public class Client extends AbstractActor {
    Main clientInfo = new Main();

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder() //receives reply from broker
                .match(ApplicationResponse.class,
                        msg -> {
                            clientInfo.printApplicationResponse(msg);
                        }).build();
    }
}