package org.darion.yaphet.akka.fsm

object CoffeeProtocol {

  trait UserInteraction

  trait VendorInteraction

  case class Deposit(value: Int) extends UserInteraction

  case class Balance(value: Int) extends UserInteraction

  case object Cancel extends UserInteraction

  case object BrewCoffee extends UserInteraction

  case object GetCostOfCoffee extends UserInteraction

  case object ShutDownMachine extends VendorInteraction

  case object StartUpMachine extends VendorInteraction

  case class SetNumberOfCoffee(quantity: Int) extends VendorInteraction

  case class SetCostOfCoffee(price: Int) extends VendorInteraction

  case object GetNumberOfCoffee extends VendorInteraction

  case class MachineError(errorMsg: String)

}
