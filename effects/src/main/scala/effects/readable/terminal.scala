package effects.readable

import scala.concurrent.{ExecutionContext, Future}
import Data._

object Data {
  type Id[A] = A
}

trait SyncConsole {

  def read: String

  def write(toWrite: String)
}

trait AsyncConsole {

  def read: Future[String]

  def write(toWrite: String)

}

trait Console[F[_]] {

  def read: F[String]

  def write(toWrite: String): F[Unit]
}

object Async2Console extends Console[Future] {
  override def read: Future[String] = ???

  override def write(toWrite: String): Future[Unit] = ???
}

object Sync2Console extends Console[Id] {

  override def read: Id[String] = ???

  override def write(toWrite: String): Id[Unit] = ???
}

trait Execution[C[_]] {

  def chain[A, B](c: C[A])(f: A => C[B]): C[B]

  def create[B](b: B): C[B]
}

object Execution {

  implicit val idExecution: Execution[Id] = new Execution[Id] {
    override def chain[A, B](c: Id[A])(f: A => B): Id[B] = f(c)

    override def create[B](b: B): Id[B] = b
  }

  implicit def futureExecution(implicit ec: ExecutionContext): Execution[Future] = new Execution[Future] {
    override def chain[A, B](c: Future[A])(f: A => Future[B]): Future[B] = c.flatMap(f)

    override def create[B](b: B): Future[B] = Future.successful(b)
  }

  implicit class Ops[A, C[_]](c: C[A]) {

    def flatMap[B](f: A => C[B])(implicit e: Execution[C]): C[B] = e.chain(c)(f)

    def map[B](f: A => B)(implicit e: Execution[C]): C[B] = e.chain(c)(r => e.create(f(r)))
  }

}

object EchoServer {

  import Execution.Ops

  def echo[C[_]](t: Console[C])(implicit ex: Execution[C]): C[String] =
    for {
      message <- t.read
      _ <- t.write(message)
    } yield message

}

object Main {

  import ExecutionContext.Implicits.global
  import Execution._

  val asyncConsole = Async2Console

  val syncConsole = Sync2Console

  EchoServer.echo(asyncConsole)
  EchoServer.echo(syncConsole)
}
