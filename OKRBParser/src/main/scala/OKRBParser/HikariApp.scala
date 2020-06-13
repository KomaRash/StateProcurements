package OKRBParser

import OKRBParser.config.{DatabaseConfig, RepositoryConfig}
import OKRBParser.domain.parseExcel.okrb.OKRBParseService
import OKRBParser.infrastructure.endpoints.OKRBEndpoints
import OKRBParser.infrastructure.parseExcel.ParseErrorInterpreter
import OKRBParser.infrastructure.parseExcel.okrb.OKRBParseInterpreter
import OKRBParser.infrastructure.repository.MySql.MySqlOKRBRepositoryInterpreter
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
    parseAlgebra= new ParseErrorInterpreter[F]()
    excelParser=new OKRBParseInterpreter[F](parseAlgebra)
    fixedThreadPool  <- (ExecutionContexts.fixedThreadPool[F](databaseConfig.poolSize))
    transactor<-(RepositoryConfig.transactor(databaseConfig,fixedThreadPool,blocker))
    _<-Resource.liftF(RepositoryConfig.initDb(transactor))
    database=new MySqlOKRBRepositoryInterpreter[F](transactor,10)
    service= new OKRBParseService[F](database,excelParser)
    okrbEndpoint=new OKRBEndpoints[F](service)
    server <- (BlazeServerBuilder[F]
      .bindHttp(8080,"127.0.0.1")
      .withHttpApp(okrbEndpoint.endpoints().orNotFound)
      .resource)
  }yield (server)
}
