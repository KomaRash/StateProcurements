package OKRBParser.domain.position

import cats.data.EitherT

trait UserValidationAlgebra[F[_]] {
  // def positionDoesNotExist(position: Position):EitherT[F,PositionAlreadyExist ,Unit] = ???
  def doesNotExist(user: User): EitherT[F, UserAlreadyExist, Unit]

  def correctDataInput(user: User): EitherT[F, CorrectUserData, Unit]
}
class UserValidationInterpreter[F[_]](repository: UserRepositoryAlgebra[F])
  extends UserValidationAlgebra[F]{
  override def doesNotExist(user: User): EitherT[F, UserAlreadyExist, Unit] = ???

  override def correctDataInput(user: User): EitherT[F, CorrectUserData, Unit] = ???
}
