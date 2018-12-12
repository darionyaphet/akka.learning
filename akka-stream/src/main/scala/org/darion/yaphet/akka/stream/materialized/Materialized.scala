package org.darion.yaphet.akka.stream.materialized

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import org.darion.yaphet.akka.stream.twitter.{Author, Hashtag, Tweet}

import scala.concurrent.Future

object Materialized {
  def main(args: Array[String]): Unit = {
    val akkaTag = Hashtag("#akka")
    val tweets: Source[Tweet, NotUsed] = Source(
      Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
        Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
        Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
        Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
        Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
        Nil)

    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ ⇒ 1)
    val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

    val counterGraph: RunnableGraph[Future[Int]] =
      tweets
        .via(count)
        .toMat(sumSink)(Keep.right)

    val sum: Future[Int] = counterGraph.run()
    sum.foreach(c ⇒ println(s"Total tweets processed: $c"))
  }
}
