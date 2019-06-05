package effects.improved

import scala.concurrent.{ExecutionContext, Future}
import Data._

object Data {
  type Id[A] = A
}

/**
 * The higher kinded type F[_] allows us to abstract away from the execution context.
 */
trait Console[F[_]] {

  def read: F[String]

  def write(toWrite: String): F[Unit]
}

case class AsyncConsole(implicit ec: ExecutionContext) extends Console[Future] {
  override def read: Future[String] = Future {
    scala.io.StdIn.readLine()
  }

  override def write(toWrite: String): Future[Unit] = Future {
    println(toWrite)
  }
}

object SyncConsole extends Console[Id] {

  override def read: Id[String] = scala.io.StdIn.readLine()

  override def write(toWrite: String): Id[Unit] = println(toWrite)
}

/**
 *
 * @tparam C
 */
trait Execution[C[_]] {

  def chain[A, B](c: C[A])(f: A => C[B]): C[B]

  def create[B](b: B): C[B]
}

object Execution {

  implicit val idExecution: Execution[Id] = new Execution[Id] {
    // TODO
    override def chain[A, B](c: Id[A])(f: A => B): Id[B] = ???

    // TODO
    override def create[B](b: B): Id[B] = ???
  }

  implicit def futureExecution(implicit ec: ExecutionContext): Execution[Future] = new Execution[Future] {
    // TODO
    override def chain[A, B](c: Future[A])(f: A => Future[B]): Future[B] = ???

    // TODO
    override def create[B](b: B): Future[B] = ???
  }
}

object EchoServer {

  def echo[C[_]](t: Console[C])(implicit ex: Execution[C]): C[String] =
    ex.chain(t.read) { in: String =>
      ex.chain(t.write(in)) { _: Unit =>
        ex.create(in)
      }
    }

}
