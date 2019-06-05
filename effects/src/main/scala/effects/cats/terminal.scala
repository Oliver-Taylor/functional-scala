package effects.cats

import cats.{Applicative, Id, Monad}
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

trait Console[F[_]] {

  def read: F[String]

  def write(toWrite: String): F[Unit]
}

object Console {

  def apply[F[_]](implicit C: Console[F]): Console[F] = C
}

object SyncConsole extends Console[Id] {
  override def read: Id[String] = scala.io.StdIn.readLine()

  override def write(toWrite: String): Id[Unit] = println(toWrite)
}

case class AsyncConsole(implicit ec: ExecutionContext) extends Console[Future] {
  override def read: Future[String] = Future {
    scala.io.StdIn.readLine()
  }

  override def write(toWrite: String): Future[Unit] = Future {
    println(toWrite)
  }
}

object IOConsole extends Console[IO] {

  override def read: IO[String] = IO.pure(scala.io.StdIn.readLine())

  override def write(toWrite: String): IO[Unit] = IO.pure(println(toWrite))
}

object EchoServer {

  def echo[C[_] : Monad : Console]: C[String] = {
    for {
      _ <- Console[C].write("Write Something")
      read <- Console[C].read
      _ <- Console[C].write(read)
    } yield read
  }

}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    import ExecutionContext.Implicits.global

    implicit val syncConsole: Console[Id] = SyncConsole
    implicit val asyncConsole: Console[Future] = AsyncConsole()
    implicit val ioConsole: Console[IO] = IOConsole

    println("Id Effect")
    EchoServer.echo[Id]

    println("Future Effect")
    Await.result(EchoServer.echo[Future], 10.seconds)

    println("IO Effect")
    EchoServer.echo[IO].map(resp => resp match {
      case "fail" => ExitCode.Error
      case _ => ExitCode.Success
    })
  }

}
