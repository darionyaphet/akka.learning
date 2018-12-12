package org.darion.yaphet.akka.stream.reactive

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import org.darion.yaphet.akka.stream.twitter.{Author, Hashtag, Tweet}

object Reactive {
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

    val authors: Source[Author, NotUsed] =
      tweets
        .filter(_.hashtags.contains(akkaTag))
        .map(_.author)
    authors.runWith(Sink.foreach(println))

    val hashtags: Source[Hashtag, NotUsed] = tweets.mapConcat(_.hashtags.toList)

    val writeAuthors: Sink[Author, NotUsed] = ???
    val writeHashtags: Sink[Hashtag, NotUsed] = ???
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import akka.stream.scaladsl.GraphDSL.Implicits._

      val bcast = b.add(Broadcast[Tweet](2))
      tweets ~> bcast.in
      bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
      bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags
      ClosedShape
    })
    g.run()


  }
}
