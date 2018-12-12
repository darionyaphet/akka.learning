package org.darion.yaphet.akka.hotswap

import akka.actor.Actor

class HotSwapActor extends Actor {

  import context._

  def angry: Receive = {
    case "foo" ⇒ sender() ! "I am already angry?"
    case "bar" ⇒ become(happy)
  }

  def happy: Receive = {
    case "bar" ⇒ sender() ! "I am already happy :-)"
    case "foo" ⇒ become(angry)
  }

  override def receive: Receive = {
    case "foo" ⇒ become(angry)
    case "bar" ⇒ become(happy)
  }
}
