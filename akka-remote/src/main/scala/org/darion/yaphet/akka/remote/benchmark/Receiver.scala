package org.darion.yaphet.akka.remote.benchmark

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Receiver {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system", ConfigFactory.load("lookup.conf"))
    system.actorOf(Props[Receiver], "Receiver")
  }
}

class Receiver extends Actor {
  override def receive: Receive = {
    //    case echo: Echo
    case _ => println()
  }
}