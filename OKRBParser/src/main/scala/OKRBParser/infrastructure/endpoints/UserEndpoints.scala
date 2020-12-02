package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.position.{User, UserService}
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, ApiVersion => _}
import tsec.authentication._
class UserEndpoints[F[_] : ConcurrentEffect : Monad](service: UserService[F],
                                                     authService: AuthService[F])
  extends Http4sDsl[F] {
  implicit val userEncoder: EntityDecoder[F, User] = jsonOf[F, User]

  private def getUserList: AuthEndpoint[F, Token] = {
    case GET -> Root asAuthed _ =>
      service.getUserList().flatMap(Ok(_))
  }

  def endpoints: HttpRoutes[F] = {
    val directorEndpoints = {
      Auth.directorOnly(getUserList)
    }
    authService.auth.liftService(directorEndpoints)
  }
}

object UserEndpoints {
  def endpoints[F[_] : ConcurrentEffect : Monad](service: UserService[F],
                                                 authService: AuthService[F]
                                                ): HttpRoutes[F] = {
    new UserEndpoints[F](service, authService).endpoints
  }
}
