package org.darion.yaphet.java.akka.stream.buffer;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.stream.*;
import akka.stream.javadsl.*;

import java.time.Duration;
import java.util.Arrays;

public class BufferMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("BufferMain");
        ActorMaterializerSettings settings = ActorMaterializerSettings.create(system).withInputBuffer(64, 64);
        Materializer materializer = ActorMaterializer.create(settings, system);

        Source
                .from(Arrays.asList(1, 2, 3))
                .map(i -> {
                    System.out.println("A: " + i);
                    return i;
                }).async()
                .map(i -> {
                    System.out.println("B: " + i);
                    return i;
                }).async()
                .map(i -> {
                    System.out.println("C: " + i);
                    return i;
                }).async()
                .runWith(Sink.ignore(), materializer);

        Duration one = Duration.ofSeconds(1L);
        Source<String, Cancellable> msgSource = Source.tick(one, one, "message!");
        Source<String, Cancellable> tickSource = Source.tick(one.multipliedBy(3), one.multipliedBy(3), "tick");
        Flow<String, Integer, NotUsed> conflate = Flow.of(String.class)
                .conflateWithSeed(first -> 1, (count, element) -> count + 1);

        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            FanInShape2<String, Integer, Integer> zipper = b.add(
                    ZipWith.create((String tick, Integer count) -> count).async());
            b.from(b.add(msgSource)).via(b.add(conflate)).toInlet(zipper.in1());
            b.from(b.add(tickSource)).toInlet(zipper.in0());
            b.from(zipper.out()).to(b.add(Sink.foreach(element -> System.out.println(element))));
            return ClosedShape.getInstance();
        })).run(materializer);
    }
}
