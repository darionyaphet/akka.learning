package org.darion.yaphet.akka

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}

class TerminatorActor(ref: ActorRef) extends Actor with ActorLogging {
  context.watch(ref)

  override def receive: Receive = {
    case Terminated(_) => {
      log.info("{} has terminated, shutting down system", ref.path)
      context.system.terminate()
    }
  }
}
