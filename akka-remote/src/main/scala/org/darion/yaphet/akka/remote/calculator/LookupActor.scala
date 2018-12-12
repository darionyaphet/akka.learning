package org.darion.yaphet.akka.remote.calculator

import scala.concurrent.duration._
import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, Identify, ReceiveTimeout, Terminated}

class LookupActor extends Actor with ActorLogging {

  sendIdentifyRequest()

  def sendIdentifyRequest(path: String): Unit = {
    context.actorSelection(path) ! Identify(path)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  override def receive = identifying

  def identifying: Actor.Receive = {
    case ActorIdentity(`path`, Some(actor)) => {
      context.watch(actor)
      context.become(active(actor))
    }

    case ActorIdentity(`path`, None) => println(s"Remote actor not available: $path")
    case ReceiveTimeout => sendIdentifyRequest()
    case _ => println("Not ready yet")
  }

  def active(actor: ActorRef): Actor.Receive = {
    case op: MathOp => actor ! op

    case result: MathResult => result match {
      case AddResult(num1, num2, result) =>
        log.info("Add result: %d + %d = %d\n", num1, num2, result)

      case SubtractResult(num1, num2, result) =>
        log.info("Sub result: %d - %d = %d\n", num1, num2, result)
    }

    case Terminated(`actor`) => {
      log.info("Calculator terminated")
      sendIdentifyRequest()
      context.become(identifying)
    }

    case ReceiveTimeout => log.info("Receive Timeout")
  }
}
