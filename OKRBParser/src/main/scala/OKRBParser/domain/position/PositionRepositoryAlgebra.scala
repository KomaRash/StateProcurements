package OKRBParser.domain.position

import OKRBParser.domain.parseExcel.RepositoryAlgebra
import cats.data.OptionT

trait PositionRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F]{
  def getUser(id: PositionId): F[Option[Position]]

  def createPosition(position: Position):
  def addUser(position: Position,userInfo:User)

}

