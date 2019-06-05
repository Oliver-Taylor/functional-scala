package effects.flawed

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

trait SyncConsole {

  def read: String = scala.io.StdIn.readLine()

  def write(toWrite: String): Unit = println(toWrite)
}

object SyncConsole extends SyncConsole

trait AsyncConsole {

  def read: Future[String]

  def write(toWrite: String): Future[Unit]

}

case class AsyncConsoleImpl(implicit ec: ExecutionContext) extends AsyncConsole {
  override def read: Future[String] = Future {
    scala.io.StdIn.readLine()
  }

  override def write(toWrite: String): Future[Unit] = Future {
    println(toWrite)
  }
}

object EchoServer {

  def syncEcho(syncConsole: SyncConsole): String = {
    val read = syncConsole.read
    syncConsole.write(read)
    read
  }

  def asyncEcho(asyncConsole: AsyncConsole)(implicit ec: ExecutionContext): Future[String] =
    for {
      read <- asyncConsole.read
      _ <- asyncConsole.write(read)
    } yield read
}

object Main {

  def main(args: Array[String]): Unit = {
    import ExecutionContext.Implicits.global

    Await.result(EchoServer.asyncEcho(AsyncConsoleImpl()), 10 seconds)
    EchoServer.syncEcho(SyncConsole)
  }
}
