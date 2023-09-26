package service.dodgydrivers;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Quoter;
import service.message.Init;

public class Main {
    public static void main(String[] args) {  //creates actor system
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "dodgydrivers"); //actor reference
        ref.tell(new Init(new DDQService()), null);
        ActorSelection selection =
                system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker"); //registration
        selection.tell("register", ref);
    }
}
