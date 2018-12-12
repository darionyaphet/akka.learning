package org.darion.yaphet.akka.remote.calculator

import akka.actor.{Actor, ActorLogging}

class CalculatorActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Add(num1, num2) =>
      val result = num1 + num2
      log.info("Calculating %d + %d = %d".format(num1, num2, result))
      sender() ! AddResult(num1, num2, result)

    case Subtract(num1, num2) =>
      val result = num1 - num2
      log.info("Calculating %d - %d = %d".format(num1, num2, result))
      sender() ! SubtractResult(num1, num2, result)

    case Multiply(num1, num2) =>
      val result = num1 * num2
      log.info("Calculating %d * %d = %d".format(num1, num2, result))
      sender() ! MultiplyResult(num1, num2, result)

    case Divide(num1, num2) =>
      val result = num1 / num2
      log.info("Calculating %d / %d = %d".format(num1, num2, result))
      sender() ! DivideResult(num1, num2, result)
  }
}
