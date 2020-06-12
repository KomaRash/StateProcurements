package OKRBParser

import OKRBParser.config.DatabaseConfig
import OKRBParser.database._
import OKRBParser.excelParser.Algebra.ExcelParseErrorInterpreter
import OKRBParser.excelParser.ExcelParser
import OKRBParser.service.EndPoints.OKRBService
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.syntax.functor._
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Server => H4Server}
import org.http4s.syntax.kleisli._

object TestApp extends IOApp {
  val databaseConfig=DatabaseConfig("com.mysql.jdbc.Driver",     // driver classname
    "jdbc:mysql://localhost:3306/ stateproducements?useUnicode=true&serverTimezone=UTC",     // connect URL (driver-specific)
    "RootKoma",                  // user
    "root",  //password
    32)
  override def run(args: List[String]): IO[ExitCode] = {
    stream[IO].use(_ => IO.never).as(ExitCode.Success)
  }

  def stream[F[_]: ConcurrentEffect: ContextShift: Timer]:Resource[F, H4Server[F]] =for{
    blocker<-(Blocker[F])
    parseAlgebra= new ExcelParseErrorInterpreter[F]()
    excelParser=new ExcelParser[F](parseAlgebra)
    fixedThreadPool  <- (ExecutionContexts.fixedThreadPool[F](databaseConfig.poolSize))
    transactor<-(RepositoryConfig.transactor(databaseConfig,fixedThreadPool,blocker))
    _<-Resource.liftF(RepositoryConfig.initDb(transactor))
    database=new MySqlRepository[F](transactor)
    okrbService=new OKRBService[F](excelParser,database)
    server <- (BlazeServerBuilder[F]
      .bindHttp(8080,"127.0.0.1")
      .withHttpApp(okrbService.service.orNotFound)
      .resource)
  }yield (server)
}
