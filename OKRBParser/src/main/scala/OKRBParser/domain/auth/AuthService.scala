package OKRBParser.domain.auth

import OKRBParser.domain.position.{User, UserId, UserRepositoryAlgebra, UsernamePasswordCredentials}
import cats.effect.Sync
import tsec.authentication.{BearerTokenAuthenticator, SecuredRequestHandler, TSecBearerToken}

class AuthService[F[_] : Sync](userRepository: UserRepositoryAlgebra[F],
                               authRepository: AuthRepositoryAlgebra[F]) {

  import Auth._

  val bearerTokenAuthenticator: BearerTokenAuthenticator[F, UserId, User] =
    BearerTokenAuthenticator[F, UserId, User](
      tokenStore(authRepository),
      userStore(userRepository),
      authSettings)


  def checkPassword(user: UsernamePasswordCredentials): F[Option[User]] = {

    userRepository.checkPassword(user)
  }

  def auth: SecuredRequestHandler[F, UserId, User, TSecBearerToken[UserId]] = SecuredRequestHandler(bearerTokenAuthenticator)


}
