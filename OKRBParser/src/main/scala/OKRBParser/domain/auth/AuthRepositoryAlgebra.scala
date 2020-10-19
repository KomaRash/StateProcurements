package OKRBParser.domain.auth

import OKRBParser.domain.parseExcel.RepositoryAlgebra
import OKRBParser.domain.position.{PositionId, UserId}
import tsec.authentication.TSecBearerToken
import tsec.common.SecureRandomId

trait AuthRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F] {
  def update(v: TSecBearerToken[UserId]): F[TSecBearerToken[UserId]]

  def put(elem: TSecBearerToken[PositionId]): F[TSecBearerToken[PositionId]]

  def delete(id: SecureRandomId): F[Unit]

  def get(id: SecureRandomId): F[Option[TSecBearerToken[PositionId]]]
}
