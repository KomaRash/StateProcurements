package OKRBParser.domain.position

import OKRBParser.domain.parseExcel.RepositoryAlgebra

trait PositionRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F]{
  def createPosition(position: Position)
  def addUser(position: Position,userInfo:User)
  def updateUserInfo(position: Position,newUserInfo:User)

}

