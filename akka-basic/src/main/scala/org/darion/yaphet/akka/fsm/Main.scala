package org.darion.yaphet.akka.fsm

import akka.actor.FSM.SubscribeTransitionCallBack
import akka.actor.{ActorSystem, Props}
import org.darion.yaphet.akka.fsm.CoffeeProtocol._

/**
  * https://rerun.me/2016/05/22/akka-notes-finite-state-machines-1/
  * https://rerun.me/2016/05/23/akka-notes-finite-state-machines-2/
  */
object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("coffee-system")
    val coffeeMachine = system.actorOf(Props(new CoffeeMachine()))

    coffeeMachine ! SetCostOfCoffee(7)
    coffeeMachine ! GetCostOfCoffee





    //    coffeeMachine ! SetCostOfCoffee(5)
    //    coffeeMachine ! SetNumberOfCoffee(10)
    //    coffeeMachine ! SubscribeTransitionCallBack(testActor)
  }
}
