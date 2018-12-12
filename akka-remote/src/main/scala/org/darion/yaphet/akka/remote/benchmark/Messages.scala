package org.darion.yaphet.akka.remote.benchmark

object Messages {

  private case object Warmup

  case object Shutdown

  sealed trait Echo

  case object Start extends Echo

  case object Done extends Echo

  case class Continue(remaining: Int, statTime: Long, burstStartTime: Long, n: Int) extends Echo

}
