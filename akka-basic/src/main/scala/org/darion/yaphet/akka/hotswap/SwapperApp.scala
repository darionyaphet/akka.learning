package org.darion.yaphet.akka.hotswap

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

case object Swap

class Swapper extends Actor {

  import context._

  val log = Logging(system, this)

  override def receive: Receive = {
    case Swap => {
      log.info("Hi")
      become({
        case Swap =>
          log.info("Ho")
          unbecome()
      }, discardOld = false)
    }
  }
}

/**
  * https://doc.akka.io/docs/akka/snapshot/actors.html?language=scala#become-unbecome
  */
object SwapperApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SwapperSystem")
    val swap = system.actorOf(Props[Swapper], name = "swapper")

    swap ! Swap // logs Hi
    swap ! Swap // logs Ho
    swap ! Swap // logs Hi
    swap ! Swap // logs Ho
    swap ! Swap // logs Hi
    swap ! Swap // logs Ho

    system.terminate()
  }
}
