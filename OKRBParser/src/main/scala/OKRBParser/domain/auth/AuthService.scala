package OKRBParser.domain.auth

import OKRBParser.domain.position.{Position, PositionId, PositionRepositoryAlgebra}
import cats.effect.Sync
import tsec.authentication.BearerTokenAuthenticator
class AuthService[F[_]:Sync](positionRepository:PositionRepositoryAlgebra[F],
                        authRepository:AuthRepositoryAlgebra[F]) {
  import Auth._
  def bearerTokenAuthenticator: BearerTokenAuthenticator[F, PositionId, Position] =
    BearerTokenAuthenticator[F, PositionId, Position](
    tokenStore(authRepository),
    userStore(positionRepository),
    authSettings)

}
