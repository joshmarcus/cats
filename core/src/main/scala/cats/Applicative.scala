package cats

import simulacrum._

/**
 * Applicative functor.
 *
 * Allows application of a function in an Applicative context to a value in an Applicative context
 *
 * See: http://www.cs.ox.ac.uk/jeremy.gibbons/publications/iterator.pdf
 * Also: http://staff.city.ac.uk/~ross/papers/Applicative.pdf
 *
 * Must obey the following laws:
 *  - apply(fa)(pure(a => a)) = fa
 *  - apply(pure(a))(pure(f)) = pure(f(a))
 *  - apply(pure(a))(ff) = apply(ff)(pure(f => f(a)))
 *  - map(fa)(f) = apply(fa)(pure(f))
 */
@typeclass trait Applicative[F[_]] extends Apply[F] { self =>

  /**
   * `pure` lifts any value into the Applicative Functor
   *
   * Applicative[Option].pure(10) = Some(10)
   */
  def pure[A](x: A): F[A]

  override def map[A, B](fa: F[A])(f: A => B): F[B] = apply(fa)(pure(f))

  /**
   * Two sequentially dependent Applicatives can be composed.
   *
   * The composition of Applicatives `F` and `G`, `F[G[x]]`, is also an Applicative
   *
   * Applicative[Option].compose[List].pure(10) = Some(List(10))
   */
  def compose[G[_]](implicit GG : Applicative[G]): Applicative[λ[α => F[G[α]]]] =
    new CompositeApplicative[F,G] {
      implicit def F: Applicative[F] = self
      implicit def G: Applicative[G] = GG
    }
}

trait CompositeApplicative[F[_],G[_]]
    extends Applicative[λ[α => F[G[α]]]] with CompositeApply[F,G] {

  implicit def F: Applicative[F]
  implicit def G: Applicative[G]

  def pure[A](a: A): F[G[A]] = F.pure(G.pure(a))
}
