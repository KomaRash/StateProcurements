package OKRBParser.Database

import OKRBParser.Config.DatabaseConfig
import cats.effect.{Async, Blocker, ContextShift, Resource}
import doobie.hikari.HikariTransactor

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
}
