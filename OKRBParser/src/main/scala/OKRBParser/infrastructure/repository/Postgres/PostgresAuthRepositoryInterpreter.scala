package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.auth.AuthRepositoryAlgebra
import OKRBParser.domain.position.PositionId
import tsec.authentication.TSecBearerToken
import tsec.common.SecureRandomId

class PostgresAuthRepositoryInterpreter[F[_]] extends AuthRepositoryAlgebra[F]{
  override def delete(id: SecureRandomId): F[Unit] = ???

  override def get(id: SecureRandomId): F[Option[TSecBearerToken[PositionId]]] = ???


  override def put(elem: TSecBearerToken[PositionId]): F[TSecBearerToken[PositionId]] = ???

  override def maxThreadPool(): Int = ???
}
