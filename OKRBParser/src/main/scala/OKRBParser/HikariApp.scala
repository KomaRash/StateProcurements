package OKRBParser

import OKRBParser.Database.MySqlRepository
import OKRBParser.Service.OKRBService
import OKRBParser.excelParser.ExcelParser
import cats.effect.{Blocker, IOApp, Resource}
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._

object HikariApp extends IOApp {
/*

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[IO, HikariTransactor[IO]] =
  for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    be <- Blocker[IO]    // our blocking EC
    xa <- HikariTransactor.newHikariTransactor[IO](
      "com.mysql.jdbc.Driver",     // driver classname
      "jdbc:mysql://localhost:3306/ StateProcurements?useUnicode=true&serverTimezone=UTC",     // connect URL (driver-specific)
      "RootKoma",                  // user
      "root",                                     // password
      ce,                                     // await connection here
      be                                      // execute JDBC operations here
    )
  } yield xa
*/

  import cats.effect.{ExitCode, IO}
  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }.orNotFound
  def run(args: List[String]): IO[ExitCode] = {
    val oKRBParser=new ExcelParser[IO]()
    val transactor: Resource[IO, HikariTransactor[IO]] =
      for {
        ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
        be <- Blocker[IO]    // our blocking EC
        xa <- HikariTransactor.newHikariTransactor[IO](
          "com.mysql.jdbc.Driver",     // driver classname
          "jdbc:mysql://localhost:3306/ StateProcurements?useUnicode=true&serverTimezone=UTC",     // connect URL (driver-specific)
          "RootKoma",                  // user
          "root",                                     // password
          ce,                                     // await connection here
          be                                      // execute JDBC operations here
        )
      } yield xa
    transactor.use{
    tx=>val okrbService=new OKRBService[IO](oKRBParser,new MySqlRepository[IO](tx))
      BlazeServerBuilder[IO]
        .bindHttp(8080, "localhost")
        .withHttpApp(okrbService.service.orNotFound)
        .resource
        .use(_ => IO.never)
        .as(ExitCode.Success)

    /*def writeToDatabase[F[_]: Sync](chunk: Chunk[Int])(tx: Transactor[F]): F[Int] =
      {
        val sql="insert into table_name (test) values (?)"
        Update[Int](sql).updateMany(chunk).transact(tx)
      }


    transactor.use { tx =>
      fs2.Stream.emits(1 to 15600)
        .chunkN(500)
        .covary[IO]
        .parEvalMap(10)(writeToDatabase[IO](_)(tx))
        .compile.drain.unsafeRunSync()


      IO(ExitCode.Success)
    }
*/}
  }
}/*
  override def run(args: List[String]): IO[ExitCode] = {
  fs2.Stream.emits(1 to 10000)
  .chunkN(10)
  .covary[IO]
  .parEvalMap(10)(writeToDatabase[IO])
  .compile.drain.unsafeRunSync()
  IO(ExitCode.Success)
}*/