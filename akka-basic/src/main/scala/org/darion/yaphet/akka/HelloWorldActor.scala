package org.darion.yaphet.akka

import akka.actor.{Actor, Props}

class HelloWorldActor extends Actor {

  override def preStart(): Unit = {
    val greeter = context.actorOf(Props[Greeter], "greeter")
    greeter ! Greeter.Greet
  }

  override def receive: Receive = {
    case Greeter.Done => context.stop(self)
  }

  override def postStop(): Unit = {
    super.postStop()
  }
}
