package org.darion.yaphet.akka.stream.dynamic

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, DelayOverflowStrategy, KillSwitches}
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

object Shutdown {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("Shutdown")
    implicit val materializer = ActorMaterializer()

    val countingSrc = Source(Stream.from(1)).delay(1.second, DelayOverflowStrategy.backpressure)
    val lastSnk = Sink.last[Int]

    val (killSwitch, last) = countingSrc
      .viaMat(KillSwitches.single)(Keep.right)
      .toMat(lastSnk)(Keep.both)
      .run()

    killSwitch.shutdown()
    Await.result(last, 1.second)
  }
}
