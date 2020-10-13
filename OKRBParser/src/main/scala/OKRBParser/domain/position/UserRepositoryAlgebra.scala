package OKRBParser.domain.position

import OKRBParser.domain.parseExcel.RepositoryAlgebra

trait UserRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F]{
  def getUser(id: UserId): F[Option[User]]
  def checkPassword(usernamePasswordCredentials: UsernamePasswordCredentials):F[Option[User]]
  def createPosition(position: Position)
  def addUser(userInfo:User)

}

