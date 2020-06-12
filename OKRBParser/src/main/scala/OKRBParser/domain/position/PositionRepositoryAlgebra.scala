package OKRBParser.domain.position

trait PositionRepositoryAlgebra[F[_]] {
  def create(position:Position):F[Position]
  def updateUserInfo(position: Position,newUserInfo: UserInfo):F[Position]
  def deleteUserInfo(position: Position):F[Position]
}
