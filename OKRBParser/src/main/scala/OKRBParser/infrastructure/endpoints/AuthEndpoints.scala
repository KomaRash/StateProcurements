package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.position.{Position, User, UserId, UsernamePasswordCredentials}
import cats.effect.Sync
import cats.implicits._
import cats.{Monad, MonadError}
import io.circe._
import io.circe.syntax._

import scala.concurrent.duration._
import io.circe.generic.semiauto._
import org.http4s.circe.jsonOf
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.{CORS, CORSConfig}
import org.http4s._
import tsec.authentication.{TSecAuthService, TSecBearerToken, _}
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
  val methodConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = false,
    allowedMethods = Some(Set("GET", "POST")),
    allowCredentials = true,
    maxAge = 1.day.toSeconds)
  val director: TSecAuthService[User, TSecBearerToken[UserId], F] =Auth.userOnly(implicitly[MonadError[F,Throwable]])(directorEndpoint)
  def endpoint: HttpRoutes[F] =CORS(loginRoute,methodConfig)<+>authService.auth.liftService((director))
  def loginRoute: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        (for {
          user<-req.as[UsernamePasswordCredentials]
          userOpt<-authService.checkPassword(user)
        }yield userOpt).flatMap {
          case Some(user) =>
            user.userID match {
              case Some(id)=>
                authService.
                bearerTokenAuthenticator.
                create(id).
                map {authService.bearerTokenAuthenticator.embed(
                  Response(Status.Ok,
                    headers=Headers.of(
                      Header("Access-Control-Allow-Origin", "http://localhost:4201"),
                      Header("Access-Control-Allow-Credentials","true"))),_).
                  withEntity(user.position.asJson)
                }
                case _=> Ok("not Found")
            }
          case None => Ok("Спасибо Thq")
        }
    }
}
