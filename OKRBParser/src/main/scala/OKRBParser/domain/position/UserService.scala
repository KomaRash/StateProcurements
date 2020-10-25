package OKRBParser.domain.position

import cats.Monad
import cats.data.EitherT

class UserService[F[_]] (userRepository: UserRepositoryAlgebra[F],
                         validation:UserValidationAlgebra[F]) {
  /*def createUser(user: User)(implicit M: Monad[F]): EitherT[F, UserError, User] ={
    for{
      _<-validation.doesNotExist(user)
      _<-validation.correctDataInput(user)
      user<-EitherT.liftF(userRepository.addUser(user))
    }yield user
  }*/
  /*def createPosition(position: Position)(implicit M:Monad[F])={
    for{
      _<-validation.positionDoesNotExist(position)
    }
  }
  /*def transferCasesAndPositions(oldUser:User,newUser:User)={

  }*/
  def add*/
}
