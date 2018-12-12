package org.darion.yaphet.akka.stream.core.demo

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object SourceDemo extends App {
  implicit val system = ActorSystem("demo")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sink: Sink[Any, Future[Done]] = Sink.foreach(println)
  val graph: RunnableGraph[NotUsed] = source.to(sink)
  val graph0: RunnableGraph[Future[Done]] = source.toMat(sink)(Keep.right)
  val result: NotUsed = graph.run()

}
