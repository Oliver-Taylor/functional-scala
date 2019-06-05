package effects.flawed

import scala.concurrent.{ExecutionContext, Future}

trait SyncConsole {

  def read: String = scala.io.StdIn.readLine()

  def write(toWrite: String): Unit = ???
}

object SyncConsole extends SyncConsole

trait AsyncConsole {

  def read: Future[String] = ???

  def write(toWrite: String): Future[Unit] = ???

}

object AsyncConsole extends AsyncConsole

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
    val syncConsole = SyncConsole
    val asyncConsole = AsyncConsole

    EchoServer.asyncEcho(asyncConsole)
    EchoServer.syncEcho(syncConsole)
  }
}
