package org.darion.yaphet.java.akka.stream;

import akka.NotUsed;
import akka.actor.Terminated;
import akka.stream.*;
import akka.stream.javadsl.*;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import scala.concurrent.Future;


public class StreamQuickstart {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("QuickStart");
        Materializer materializer = ActorMaterializer.create(system);

        Source<Integer, NotUsed> source = Source.range(1, 100);
        source.runForeach(i -> System.out.println(i), materializer);


        Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE,
                (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

        CompletionStage<IOResult> result = factorials
                .map(number -> ByteString.fromString(number.toString() + "\n"))
                .runWith(FileIO.toPath(Paths.get("/tmp/factorials")), materializer);

        CompletionStage<Done> done = source.runForeach(i -> System.out.println(i), materializer);
        done.thenRun(() -> system.terminate());

    }
}
