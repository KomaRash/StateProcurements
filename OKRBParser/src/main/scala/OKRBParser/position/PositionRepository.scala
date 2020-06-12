package OKRBParser.position

trait PositionRepository[F[_]] {
  def create(position:Position):F[Position]
  def updateUserInfo(position: Position,newUserInfo: UserInfo):F[Position]
  def deleteUserInfo(position: Position):F[Position]
}
