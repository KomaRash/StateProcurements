package OKRBParser

import OKRBParser.config.{DatabaseConfig, RepositoryConfig}
import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.purchase.PurchaseService
import OKRBParser.infrastructure.endpoints.{AuthEndpoints, PurchaseEndpoints}
import OKRBParser.infrastructure.repository.Postgres.{PostgresAuthRepositoryInterpreter, PostgresPurchaseRepositoryInterpreter, PostgresUserRepositoryInterpreter}
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.implicits._
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Server => H4Server}


object TestApp extends IOApp {
  val databaseConfig = DatabaseConfig("org.postgresql.Driver", // driver classname
    "jdbc:postgresql://localhost:5432/stateproducement", // connect URL (driver-specific)
    "gpk", // user
    "2020", //password
    32)

  override def run(args: List[String]): IO[ExitCode] = {
    stream[IO].use(_ => IO.never).as(ExitCode.Success)
  }

  /*
    def stream[F[_]: ConcurrentEffect: ContextShift: Timer]:Resource[F, H4Server[F]] =for{
      blocker<-(Blocker[F])
      parseAlgebra= new ParseErrorInterpreter[F]()
      excelParser=new OKRBParseInterpreter[F](parseAlgebra)
      fixedThreadPool  <- (ExecutionContexts.fixedThreadPool[F](databaseConfig.poolSize))
      transactor<-(RepositoryConfig.transactor(databaseConfig,fixedThreadPool,blocker))
      _<-Resource.liftF(RepositoryConfig.initDb(transactor))
      database=new PostgresOKRBRepositoryInterpreter[F](transactor,10)
      service= new OKRBParseService[F](database,excelParser)
      okrbEndpoint=new OKRBEndpoints[F](service)
      purchaseRepository=new PostgresPurchaseRepositoryInterpreter[F](transactor,10)
      purchaseService=new PurchaseService[F](purchaseRepository,new PurchaseValidationInterpreter[F](purchaseRepository))
      purchaseEndpoint=new PurchaseEndpoints[F](purchaseService)
      server <- (BlazeServerBuilder[F]
        .bindHttp(8080,"127.0.0.1")
        .withHttpApp(purchaseEndpoint.endpoint.orNotFound)
        .resource)
    }yield (server)*/
  def stream[F[_] : ConcurrentEffect : ContextShift : Timer]: Resource[F, H4Server[F]] = for {
    blocker <- (Blocker[F])
    fixedThreadPool <- (ExecutionContexts.fixedThreadPool[F](databaseConfig.poolSize))
    transactor <- (RepositoryConfig.transactor(databaseConfig, fixedThreadPool, blocker))
    _ <- Resource.liftF(RepositoryConfig.initDb(transactor))

    userRepository = new PostgresUserRepositoryInterpreter[F](transactor)
    authRepository = new PostgresAuthRepositoryInterpreter[F](transactor)
    purchaseRepository = new PostgresPurchaseRepositoryInterpreter[F](transactor, 10)
    purchaseService = PurchaseService.service(purchaseRepository)
    authService = new AuthService[F](userRepository, authRepository)
    purchaseEndpoints = PurchaseEndpoints.endpoints(purchaseService, authService)
    authEndpoints = AuthEndpoints.endpoints(authService)
    endpoints = authEndpoints <+> purchaseEndpoints

    server <- BlazeServerBuilder[F]
      .bindHttp(8080, "127.0.0.1")
      .withHttpApp(endpoints.orNotFound)
      .resource
  } yield server

}
