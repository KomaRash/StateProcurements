package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.position.UsernamePasswordCredentials
import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.{jsonOf, _}
import org.http4s.dsl.Http4sDsl

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
                          Header("Access-Control-Allow-Credentials", "true"),
                          Header("Access-Control-Allow-Headers", "Authorization"))), _).
                      withEntity(user.position.asJson)
                  }
              case _ => Ok("not Found")
            }
          case None => Conflict()
        }
    }
  def endpoints(): HttpRoutes[F] = {
    loginRoute
  }
}

object AuthEndpoints {

  def endpoints[F[_] : Sync : Monad](authService: AuthService[F]): HttpRoutes[F] = {
    new AuthEndpoints[F](authService).endpoints()
  }
}
