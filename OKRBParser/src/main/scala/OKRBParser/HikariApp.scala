package OKRBParser

import OKRBParser.config.{DatabaseConfig, RepositoryConfig}
import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.document.DocumentService
import OKRBParser.domain.parseExcel.okrb.OKRBService
import OKRBParser.domain.purchase.PurchaseService
import OKRBParser.infrastructure.endpoints.{AuthEndpoints, DocumentEndpoints, OKRBEndpoints, PurchaseEndpoints}
import OKRBParser.infrastructure.parseExcel.ParseErrorInterpreter
import OKRBParser.infrastructure.parseExcel.okrb.OKRBParseInterpreter
import OKRBParser.infrastructure.repository.Postgres.{PostgresAuthRepositoryInterpreter, PostgresDocumentRepositoryInterpreter, PostgresOKRBRepositoryInterpreter, PostgresPurchaseRepositoryInterpreter, PostgresUserRepositoryInterpreter}
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.implicits._
import doobie.util.ExecutionContexts
import org.http4s.{Header, Headers}
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{CORS, CORSConfig}
import org.http4s.server.{Router, Server => H4Server}

import scala.concurrent.duration._

object TestApp extends IOApp {
  val databaseConfig = DatabaseConfig("org.postgresql.Driver", // driver classname
    "jdbc:postgresql://localhost:5432/stateproducement", // connect URL (driver-specific)
    "gpk", // user
    "2020", //password
    32)

  override def run(args: List[String]): IO[ExitCode] = {
    stream[IO].use(_ => IO.never).as(ExitCode.Success)
  }

  def stream[F[_] : ConcurrentEffect : ContextShift : Timer]: Resource[F, H4Server[F]] = for {
    blocker <- (Blocker[F])
    fixedThreadPool <- (ExecutionContexts.fixedThreadPool[F](databaseConfig.poolSize))
    transactor <- (RepositoryConfig.transactor(databaseConfig, fixedThreadPool, blocker))
    _ <- Resource.liftF(RepositoryConfig.initDb(transactor))
    parseAlgebra= new ParseErrorInterpreter[F]()
    excelParser=new OKRBParseInterpreter[F](parseAlgebra)
    database=new PostgresOKRBRepositoryInterpreter[F](transactor,10)
    service= new OKRBService[F](database,excelParser)

    userRepository = new PostgresUserRepositoryInterpreter[F](transactor)
    authRepository = new PostgresAuthRepositoryInterpreter[F](transactor)
    purchaseRepository = new PostgresPurchaseRepositoryInterpreter[F](transactor)
    documentRepository=new PostgresDocumentRepositoryInterpreter[F](transactor)
    purchaseService = PurchaseService.service(purchaseRepository)
    authService = new AuthService[F](userRepository, authRepository)
    docService =new DocumentService[F](documentRepository)
    purchaseEndpoints = PurchaseEndpoints.endpoints(purchaseService, authService)
    authEndpoints = AuthEndpoints.endpoints(authService)
    okrbEndpoints=OKRBEndpoints.endpoints(service,authService)
    documentEndpoints=DocumentEndpoints.endpoints(docService,authService)
    serviceEndpoints=CORS(
      Router(
        "purchases"->purchaseEndpoints,
        "okrb"->okrbEndpoints,
        "auth"->authEndpoints,
        "documents"->documentEndpoints
      )).map(_.withHeaders(Headers.of(
      Header("Access-Control-Allow-Origin", "http://localhost:4200"),
      Header("Access-Control-Allow-Credentials", "true"))))
    endpoints = CORS(authEndpoints) <+>serviceEndpoints

    server <- BlazeServerBuilder[F]
      .bindHttp(8080, "127.0.0.1")
      .withHttpApp(endpoints.orNotFound)
      .resource
  } yield server

}
