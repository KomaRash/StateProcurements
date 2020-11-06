package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.position.UsernamePasswordCredentials
import cats.Monad
import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.{jsonOf, _}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.{CORS, CORSConfig}

import scala.concurrent.duration._

class AuthEndpoints[F[_] : Sync : Monad](authService: AuthService[F]) extends Http4sDsl[F] {
  implicit val usernamePasswordCredentials: EntityDecoder[F, UsernamePasswordCredentials] = jsonOf[F, UsernamePasswordCredentials]

  private def loginRoute: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req@POST -> Root / "login" =>
        (for {
          user <- req.as[UsernamePasswordCredentials]
          userOpt <- authService.checkPassword(user)
        } yield userOpt).flatMap {
          case Some(user) =>
            user.userID match {
              case Some(id) =>
                authService.
                  bearerTokenAuthenticator.
                  create(id).
                  map {
                    authService.bearerTokenAuthenticator.embed(
                      Response(Status.Ok,
                        headers = Headers.of(
                          Header("Access-Control-Allow-Origin", "http://localhost:4200"),
                          Header("Access-Control-Allow-Credentials", "true"))), _).
                      withEntity(user.position.asJson)
                  }
              case _ => Ok("not Found")
            }
          case None => Ok("Спасибо Thq")
        }
    }

  def endpoints(config: CORSConfig): HttpRoutes[F] = CORS(loginRoute, config)
}

object AuthEndpoints {
  private val methodConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = false,
    allowedMethods = Some(Set("GET", "POST")),
    allowCredentials = true,
    maxAge = 1.day.toSeconds)

  def endpoints[F[_] : Sync : Monad](authService: AuthService[F]): HttpRoutes[F] = {
    new AuthEndpoints[F](authService).endpoints(methodConfig)
  }
}
