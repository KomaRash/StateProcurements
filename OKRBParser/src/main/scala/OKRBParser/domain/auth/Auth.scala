package OKRBParser.domain.auth

import OKRBParser.domain.position.{Role, User, UserId, UserRepositoryAlgebra}
import cats.MonadError
import cats.data.OptionT
import org.http4s.Response
import tsec.authentication.{BackingStore, IdentityStore, SecuredRequest, TSecAuthService, TSecBearerToken, TSecTokenSettings}
import tsec.authorization.{Authorization, BasicRBAC}
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
      expiryDuration = 8.hours,
      maxIdle = None)
  def _allRoles[F[_],Auth]
  (implicit F:MonadError[F,Throwable]):BasicRBAC[F,Role,User,Auth]= {
    BasicRBAC.all[F,Role,User,Auth]
  }
  def _userOnly[F[_],Auth]
  (implicit F:MonadError[F,Throwable]):BasicRBAC[F,Role,User,Auth]= {
    BasicRBAC[F, Role, User, Auth](Role.User)
  }
  def _directorOnly[F[_],Auth]
  (implicit F:MonadError[F,Throwable]):BasicRBAC[F,Role,User,Auth]= {
    BasicRBAC[F, Role, User, Auth](Role.Director)
  }
  def allRoles[F[_],Auth](pf: PartialFunction[SecuredRequest[F, User,Auth ], F[Response[F]]])
                          (onNotAuthorized: TSecAuthService[User, Auth, F])
                          (implicit F:MonadError[F,Throwable]): TSecAuthService[User, Auth, F] = {
    TSecAuthService.withAuthorizationHandler[User,Auth,F](_allRoles)(pf,onNotAuthorized.run)
  }
  def roleOnly[F[_],Auth](auth:Authorization[F, User, Auth])
                         (pf: PartialFunction[SecuredRequest[F, User,Auth], F[Response[F]]])
                         (implicit F:MonadError[F,Throwable]): TSecAuthService[User, Auth, F]={
    TSecAuthService.withAuthorization(auth)(pf)
  }
  def directorOnly[F[_],Auth](implicit F:MonadError[F,Throwable])=roleOnly[F,Auth](_directorOnly)(_)
  def userOnly[F[_],Auth](implicit F:MonadError[F,Throwable])=roleOnly[F,Auth](_userOnly)(_)
}
