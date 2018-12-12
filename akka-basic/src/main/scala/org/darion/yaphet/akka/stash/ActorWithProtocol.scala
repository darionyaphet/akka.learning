package org.darion.yaphet.akka.stash

import akka.actor.{Actor, ActorLogging, Stash}

class ActorWithProtocol extends Actor with Stash with ActorLogging {
  override def receive: Receive = {
    case "open" =>
      unstashAll()
      context.become({
        case "write" => log.info("do writing...")
        case "close" => {
          unstashAll()
          context.unbecome()
        }
      }, discardOld = false)
    case message =>
      stash()
  }
}
