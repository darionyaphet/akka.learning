package org.darion.yaphet.java.akka.cluster;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimpleClusterListener extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Cluster cluster = Cluster.get(getContext().getSystem());

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
                ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterEvent.MemberUp.class, up -> log.info("Member up: {}", up.member()))
                .match(ClusterEvent.UnreachableMember.class, unreachable -> log.info("Member unreachable: {}",
                        unreachable.member()))
                .match(ClusterEvent.MemberRemoved.class, removed -> log.info("Member removed: {}", removed.member()))
                .match(ClusterEvent.MemberEvent.class, message -> {

                })
                .build();
    }
}
