package org.darion.yaphet.akka.remote.calculator

import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.util.Random

object LookupApplication {

  def main(args: Array[String]): Unit = {
    if (args.head == "Calculator") {
      val system = ActorSystem("CalculatorSystem",
        ConfigFactory.load("calculator"))
      system.actorOf(Props[CalculatorActor], "calculator")

    } else if (args.head == "Lookup") {
      val system = ActorSystem("LookupSystem", ConfigFactory.load("lookup"))
      val remotePath =
        "akka.tcp://CalculatorSystem@127.0.0.1:2552/user/calculator"
      val actor = system.actorOf(Props(classOf[LookupActor], remotePath), "lookupActor")

      import system.dispatcher
      system.scheduler.schedule(1.second, 1.second) {
        if (Random.nextInt(100) % 2 == 0)
          actor ! Add(Random.nextInt(100), Random.nextInt(100))
        else
          actor ! Subtract(Random.nextInt(100), Random.nextInt(100))
      }
    } else {
      println("args should be Calculator or Lookup")
    }
  }
}
