package org.darion.yaphet.cluster.sharding

import akka.actor.{ActorRef, ActorSystem, Props, ReceiveTimeout}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}

import scala.concurrent.duration._
import akka.persistence.PersistentActor

case object Increment

case object Decrement

case object Stop

final case class Get(counterId: Long)

final case class EntityEnvelope(id: Long, payload: Any)

final case class CounterChanged(delta: Int)

class Counter extends PersistentActor {

  import akka.cluster.sharding.ShardRegion.Passivate

  context.setReceiveTimeout(120.seconds)

  var counter = 0

  def updateState(event: CounterChanged): Unit = {
    counter += event.delta
  }

  override def receiveRecover: Receive = {
    case event: CounterChanged => updateState(event)
  }

  override def receiveCommand: Receive = {

    case Increment => persist(CounterChanged(+1))(updateState)
    case Decrement => persist(CounterChanged(-1))(updateState)
    case Get(_) => sender() ! counter
    case ReceiveTimeout => context.parent ! Passivate(stopMessage = Stop)
    case Stop => context.stop(self)
  }

  override def persistenceId: String = "Counter-" + self.path.name

}


object Counter {
  def main(args: Array[String]): Unit = {
    val extractEntityId: ShardRegion.ExtractEntityId = {
      case EntityEnvelope(id, payload) ⇒ (id.toString, payload)
      case msg@Get(id) ⇒ (id.toString, msg)
    }

    val extractEntityId: ShardRegion.ExtractEntityId = {
      case EntityEnvelope(id, payload) ⇒ (id.toString, payload)
      case msg@Get(id) ⇒ (id.toString, msg)
    }

    val system = ActorSystem("Counter")
    val counterRegion: ActorRef = ClusterSharding(system).start(
      typeName = "Counter",
      entityProps = Props[Counter],
      settings = ClusterShardingSettings(system),
      extractEntityId = extractEntityId,
      extractShardId = extractShardId)
  }
}
