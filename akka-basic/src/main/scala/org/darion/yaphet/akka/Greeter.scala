package org.darion.yaphet.akka

import akka.actor.Actor

object Greeter {

  case object Greet

  case object Done

}


class Greeter extends Actor {
  override def receive: Receive = {
    case Greeter.Greet => {
      println("Hello World!")
      sender() ! Greeter.Done
    }
  }
}
