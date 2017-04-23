
trait Profunctor[P[_, _]] {
  def lmap[A, B, X](p: P[A, B])(f: X => A): P[X, B]
  def rmap[A, B, Y](p: P[A, B])(g: B => Y): P[A, Y]
  def dimap[A, B, X, Y](p: P[A, B])(f: X => A, g: B => Y): P[X, Y]
}

trait Strong[P[_, _]] extends Profunctor[P] {
  def strongL[A, B, X](p: P[A, B]): P[(A, X), (B, X)]
  def strongR[A, B, X](p: P[A, B]): P[(X, A), (X, B)]
}

trait Choice[P[_, _]] extends Profunctor[P] {
  def choiceL[A, B, X](p: P[A, B]): P[Either[A, X], Either[B, X]]
  def choiceR[A, B, X](p: P[A, B]): P[Either[X, A], Either[X, B]]
}

object Instances {

  implicit val function1: Strong[Function1] with Choice[Function1] = new Strong[Function1] with Choice[Function1] {
    override def lmap[A, B, X](p: A => B)(f: X => A) = p compose f
    override def rmap[A, B, Y](p: A => B)(g: B => Y) = g compose p
    override def dimap[A, B, X, Y](p: A => B)(f: X => A, g: B => Y) = g compose p compose f
    override def strongL[A, B, X](p: A => B) = { case (a, x) => (p(a), x) }
    override def strongR[A, B, X](p: A => B) = { case (x, b) => (x, p(b)) }
    override def choiceL[A, B, X](p: A => B) = _.left.map(p)
    override def choiceR[A, B, X](p: A => B) = _.right.map(p)
  }

}