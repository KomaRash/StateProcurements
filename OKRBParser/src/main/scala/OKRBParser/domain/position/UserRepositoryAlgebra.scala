package OKRBParser.domain.position

trait UserRepositoryAlgebra[F[_]] {
  def getUserList: F[List[User]]

  def getUser(id: UserId): F[Option[User]]

  def checkPassword(usernamePasswordCredentials: UsernamePasswordCredentials): F[Option[User]]

  /*def createPosition(position: Position)
  def addUser(userInfo:User):F[User]*/

}

