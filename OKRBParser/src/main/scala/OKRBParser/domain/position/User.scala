package OKRBParser.domain.position

import cats.MonadError
import tsec.authorization.AuthorizationInfo

/**
 *
 * @param userID - id пользователя
 * @param position - должность пользователя
 * @param name - имя пользователя
 * @param surname - фамилия
 * @param fatherName - отчество
 * @param email - эмаил
 * @param password - пароль от учетной записи
 * @param militaryRank - воинское звание
 */
case class User(userID:Option[Int],
                position: Position,
                name:String,
                surname:String,
                fatherName:String,
                email:String,
                password:String,
                militaryRank:String)
object User {
  implicit def authRole[F[_]](implicit F: MonadError[F, Throwable]): AuthorizationInfo[F, Role, User] =
    (u: User) => F.pure(u.position.positionRole)
}

