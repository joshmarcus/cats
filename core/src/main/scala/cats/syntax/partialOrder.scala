package cats
package syntax

import cats.macros.Ops

trait PartialOrderSyntax {
  implicit def partialOrderSyntax[A: PartialOrder](a: A) = new PartialOrderOps[A](a)
}

class PartialOrderOps[A](lhs: A)(implicit A: PartialOrder[A]) {
  def >(rhs: A): Boolean = macro Ops.binop[A, Boolean]
  def >=(rhs: A): Boolean = macro Ops.binop[A, Boolean]
  def <(rhs: A): Boolean = macro Ops.binop[A, Boolean]
  def <=(rhs: A): Boolean = macro Ops.binop[A, Boolean]

  def partialCompare(rhs: A): Double = macro Ops.binop[A, Double]
  def tryCompare(rhs: A): Option[Int] = macro Ops.binop[A, Option[Int]]
  def pmin(rhs: A): Option[A] = macro Ops.binop[A, Option[A]]
  def pmax(rhs: A): Option[A] = macro Ops.binop[A, Option[A]]
}
