package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.position.{User, UserId, UsernamePasswordCredentials}
import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Response, Status}
import tsec.authentication.{TSecAuthService, TSecBearerToken, _}
class AuthEndpoints[F[_]:Sync:Monad](authService:AuthService[F])  extends Http4sDsl[F]{
  implicit val UsernamePasswordCredentialscodec: Codec[UsernamePasswordCredentials] =deriveCodec
  implicit val usernamePasswordCredentials: EntityDecoder[F, UsernamePasswordCredentials] = jsonOf[F, UsernamePasswordCredentials]

  val a=TSecAuthService[User, TSecBearerToken[UserId], F] {
    case GET -> Root / "safe-resource" asAuthed user => Ok(s"Hello ${user.email}")
  }
  def endpoint: HttpRoutes[F] =loginRoute<+>authService.auth.liftService(a)
  def loginRoute: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        (for {
          user<-req.as[UsernamePasswordCredentials]
          userOpt<-authService.checkPassword(user)
        }yield userOpt).flatMap {
          case Some(value) => authService.bearerTokenAuthenticator.create(value.userID.get).map(authService.bearerTokenAuthenticator.embed(Response(Status.Ok), _))
          case None =>(Ok("123"))
        }
    }
}
