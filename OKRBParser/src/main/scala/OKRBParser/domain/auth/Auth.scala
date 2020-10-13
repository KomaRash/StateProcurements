package OKRBParser.domain.auth

import OKRBParser.domain.position.{Position, PositionId, PositionRepositoryAlgebra}
import cats.data.OptionT
import tsec.authentication.{BackingStore, IdentityStore, TSecBearerToken, TSecTokenSettings}
import tsec.common.SecureRandomId

import scala.concurrent.duration._
object Auth {
  def userStore[F[_]](repository:PositionRepositoryAlgebra[F]): IdentityStore[F, PositionId, Position] = (id: PositionId) => {
    OptionT(repository.getUser(id))
  }
  def tokenStore[F[_]](repository:AuthRepositoryAlgebra[F]): BackingStore[F, SecureRandomId, TSecBearerToken[PositionId]] =
    new BackingStore[F,SecureRandomId,TSecBearerToken[PositionId]] {
      override def put(elem: TSecBearerToken[PositionId]): F[TSecBearerToken[PositionId]] ={
        repository.put(elem)
      }
      override def update(v: TSecBearerToken[PositionId]): F[TSecBearerToken[PositionId]] ={
        put(v)
      }
      override def delete(id: SecureRandomId): F[Unit] = repository.delete(id)

      override def get(id: SecureRandomId): OptionT[F, TSecBearerToken[PositionId]] = OptionT(repository.get(id))
    }
  def authSettings: TSecTokenSettings =
    TSecTokenSettings(
      expiryDuration = 10.minutes,
      maxIdle = None)
}
