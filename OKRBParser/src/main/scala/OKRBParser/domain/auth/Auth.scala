package OKRBParser.domain.auth

import OKRBParser.domain.position.{ UserRepositoryAlgebra, User, UserId}
import cats.data.OptionT
import tsec.authentication.{BackingStore, IdentityStore, TSecBearerToken, TSecTokenSettings}
import tsec.common.SecureRandomId

import scala.concurrent.duration._
object Auth {
  def userStore[F[_]](repository:UserRepositoryAlgebra[F]): IdentityStore[F, UserId, User] = (id: UserId) => {
    OptionT(repository.getUser(id))
  }
  def tokenStore[F[_]](repository:AuthRepositoryAlgebra[F]): BackingStore[F, SecureRandomId, TSecBearerToken[UserId]] =
    new BackingStore[F,SecureRandomId,TSecBearerToken[UserId]] {
      override def put(elem: TSecBearerToken[UserId]): F[TSecBearerToken[UserId]] ={
        repository.put(elem)
      }
      override def update(v: TSecBearerToken[UserId]): F[TSecBearerToken[UserId]] ={
        put(v)
      }
      override def delete(id: SecureRandomId): F[Unit] = repository.delete(id)

      override def get(id: SecureRandomId): OptionT[F, TSecBearerToken[UserId]] = OptionT(repository.get(id))
    }
  def authSettings: TSecTokenSettings =
    TSecTokenSettings(
      expiryDuration = 10.minutes,
      maxIdle = None)
}
