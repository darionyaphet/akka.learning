package org.darion.yaphet.akka.stream.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object RunnableGraphExample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)

    // connect the Source to the Sink, obtaining a RunnableGraph
    val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

    // materialize the flow and get the value of the FoldSink
    val sum: Future[Int] = runnable.run()
    sum.onComplete(value => println(value.get))
  }
}
