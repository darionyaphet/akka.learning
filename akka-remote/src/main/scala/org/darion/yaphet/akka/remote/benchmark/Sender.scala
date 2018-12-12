package org.darion.yaphet.akka.remote.benchmark

import akka.actor.{Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

object Sender {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("sender", ConfigFactory.load(""))
  }
}

class Sender extends Actor {
  override def receive: Receive = {

  }
}