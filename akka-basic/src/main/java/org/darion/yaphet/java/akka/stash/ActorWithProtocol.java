package org.darion.yaphet.java.akka.stash;

import akka.actor.AbstractActorWithStash;

public class ActorWithProtocol extends AbstractActorWithStash {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("open", s -> getContext().become(receiveBuilder()
                        .matchEquals("write", ws -> System.out.println("write"))
                        .matchEquals("close", cs -> {
                            System.out.println("close");
                            unstashAll();
                            getContext().unbecome();
                        })
                        .matchAny(msg -> stash())
                        .build(), false))
                .matchAny(msg -> stash())
                .build();
    }
}