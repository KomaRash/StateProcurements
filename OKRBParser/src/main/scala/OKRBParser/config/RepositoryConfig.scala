package OKRBParser.config

import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext

object RepositoryConfig {
    def transactor[F[_]: Async: ContextShift](config: DatabaseConfig,
                                              fixedThreadPool: ExecutionContext,
                                              blocker: Blocker): Resource[F, HikariTransactor[F]] =
      HikariTransactor.newHikariTransactor[F](config.driver,
        config.url,
        config.user,
        config.password,
        fixedThreadPool,
        blocker)
    def initDb[F[_]](transactor: HikariTransactor[F])(implicit S:Sync[F]): F[Unit] ={
       transactor.configure{
        hikari=>
         S.delay {
           Flyway
             .configure()
             .dataSource(hikari)
             .load()
             .migrate()
         }
         }
    }


}
