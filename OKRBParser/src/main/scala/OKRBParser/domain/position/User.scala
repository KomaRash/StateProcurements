package OKRBParser.domain.position

import cats.MonadError
import tsec.authorization.AuthorizationInfo

case class User(position: Position,
                name:String,
                surname:String,
                fatherName:String,
                email:String,
                password:String,
                militaryRank:String,
                userID:Option[UserId]=None)
object User {
  implicit def authRole[F[_]](implicit F: MonadError[F, Throwable]): AuthorizationInfo[F, Role, User] =
    (u: User) => F.pure(u.position.positionRole)
}
case class UsernamePasswordCredentials(password:String,email:String)
