package org.darion.yaphet.akka.stream

import akka.stream._
import akka.stream.scaladsl._

import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object Main {

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s ⇒ ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    // emitting the integers 1 to 10
    val source: Source[Int, NotUsed] = Source(1 to 10)
    val done: Future[Done] = source.runForeach(i ⇒ println(i))(materializer)


    val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)
    val result: Future[IOResult] =
      factorials
        .map(num ⇒ ByteString(s"$num\n"))
        .runWith(FileIO.toPath(Paths.get("factorials.txt")))


    implicit val ec = system.dispatcher
    result.onComplete(_ => system.terminate())

  }
}
