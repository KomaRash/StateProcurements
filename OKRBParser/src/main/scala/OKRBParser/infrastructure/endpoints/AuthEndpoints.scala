package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.position.{User, UserId, UsernamePasswordCredentials}
import cats.{Monad, MonadError}
import cats.effect.Sync
import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Response, Status}
import tsec.authentication.{TSecAuthService, TSecBearerToken, _}
import tsec.authentication._
class AuthEndpoints[F[_]:Sync:Monad](authService:AuthService[F])  extends Http4sDsl[F]{
  implicit val UsernamePasswordCredentialsCodec: Codec[UsernamePasswordCredentials] =deriveCodec
  implicit val usernamePasswordCredentials: EntityDecoder[F, UsernamePasswordCredentials] = jsonOf[F, UsernamePasswordCredentials]

  val a: TSecAuthService[User, TSecBearerToken[UserId], F] =TSecAuthService[User, TSecBearerToken[UserId], F] {
    case req@GET -> Root / "safe-resource" asAuthed user =>
      val r: SecuredRequest[F, User, TSecBearerToken[UserId]] = req

        authService.bearerTokenAuthenticator.renew(r.authenticator).map(authService.bearerTokenAuthenticator.embed(Response(Status.Ok), _))
  }
  def directorEndpoint: AuthEndpoint[F, TSecBearerToken[UserId]]={
    case GET -> Root / "safe-DirectorResource" asAuthed user => Ok(s"Hello ${user.position.positionRole}")
  }
  val director: TSecAuthService[User, TSecBearerToken[UserId], F] =Auth.userOnly(implicitly[MonadError[F,Throwable]])(directorEndpoint)
  def endpoint: HttpRoutes[F] =loginRoute<+>authService.auth.liftService((director))
  def loginRoute: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        (for {
          user<-req.as[UsernamePasswordCredentials]
          userOpt<-authService.checkPassword(user)
        }yield userOpt).flatMap {
          case Some(value) => authService.
            bearerTokenAuthenticator.
            create(value.userID.get).
            map(authService.bearerTokenAuthenticator.embed(Response(Status.Ok), _))
          case None => Ok("123")
        }
    }
}
