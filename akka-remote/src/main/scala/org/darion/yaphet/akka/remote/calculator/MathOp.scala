package org.darion.yaphet.akka.remote.calculator

trait MathOp

final case class Add(num1: Int, num2: Int) extends MathOp

final case class Subtract(num1: Int, num2: Int) extends MathOp

final case class Multiply(num1: Int, num2: Int) extends MathOp

final case class Divide(num1: Int, num2: Int) extends MathOp

trait MathResult

final case class AddResult(num1: Int, num2: Int, result: Int) extends MathResult

final case class SubtractResult(num1: Int, num2: Int, result: Int) extends MathResult

final case class MultiplyResult(num1: Int, num2: Int, result: Int) extends MathResult

final case class DivideResult(num1: Int, num2: Int, result: Int) extends MathResult