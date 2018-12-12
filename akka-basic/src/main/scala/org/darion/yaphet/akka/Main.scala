package org.darion.yaphet.akka

import akka.actor.{ActorSystem, Props}

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Hello")
    val hello = system.actorOf(Props[HelloWorldActor], "helloWorld")
    system.actorOf(Props(classOf[TerminatorActor], hello), "terminator")
  }
}
