package org.darion.yaphet.java.akka.stream.core;

import akka.NotUsed;
import akka.stream.javadsl.Source;

import java.util.Arrays;

public class SinkDemo {
    public static void main(String[] args) {
        Source<Integer, NotUsed> source = Source.from(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

    }
}
