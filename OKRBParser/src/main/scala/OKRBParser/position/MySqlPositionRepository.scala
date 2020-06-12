package OKRBParser.position

import cats.effect.Sync
import doobie.util.transactor.Transactor
class MySqlPositionRepository[F[_]:Sync](tx:Transactor[F]) extends PositionRepository[F]{
  override def create(position: Position): F[Position] = ???
  override def updateUserInfo(position: Position, newUserInfo: UserInfo): F[Position] = ???

  override def deleteUserInfo(position: Position): F[Position] = ???
}
