package OKRBParser.domain.position

class UserService[F[_]](repository: UserRepositoryAlgebra[F],
                        validation: UserValidationAlgebra[F]) {
  def getUserList():F[List[User]]=repository.getUserList

}
object UserService{
  def service[F[_]](repository: UserRepositoryAlgebra[F]): UserService[F] ={
    new UserService[F](repository,new UserValidationInterpreter[F](repository))
  }
}
