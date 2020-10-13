package OKRBParser.infrastructure.repository.Postgres

import java.time.Instant

import OKRBParser.domain.auth.AuthRepositoryAlgebra
import OKRBParser.domain.position.UserId

import doobie.implicits._
import doobie.implicits.legacy.instant._
import cats.Id
import cats.effect.Sync
import cats.implicits._
import doobie.util.transactor.Transactor
import doobie.util.{Put, query}
import tsec.authentication.TSecBearerToken
import tsec.common.SecureRandomId
class PostgresAuthRepositoryInterpreter[F[_]:Sync](tx:Transactor[F])
      extends AuthRepositoryAlgebra[F]{
  import AuthSQL._
  import doobie.implicits._
  override def delete(id: SecureRandomId): F[Unit] =
    deleteFromTable(id).run.transact(tx).as()
  override def get(id: SecureRandomId): F[Option[TSecBearerToken[UserId]]] =selectById(id).
    option.
    transact(tx)

  override def put(elem: TSecBearerToken[UserId]): F[TSecBearerToken[UserId]] = {
    insert(elem).run.transact(tx).as(elem)
  }

  override def maxThreadPool(): Int = ???
}
object AuthSQL{

  implicit val secureRandomIdPut: Put[SecureRandomId] =
    Put[String].contramap((_: Id[SecureRandomId]).widen)
  def selectById(id: SecureRandomId): query.Query0[TSecBearerToken[UserId]] =
    sql"""select * from bearertokens where id=$id""".
      query[(String, Long, Instant, Option[Instant])].map(x=>
      TSecBearerToken(x._1.asInstanceOf[SecureRandomId],
      x._2.asInstanceOf[UserId],
      x._3,x._4))

  def deleteFromTable(id: SecureRandomId): doobie.Update0 =
    sql"""delete from bearertokens where id=$id""".update
  def insert(elem:TSecBearerToken[UserId])=
    sql"""insert into bearertokens (id, identity, expiry, lasttouched)
         |VALUES (${elem.id},${elem.identity},${elem.expiry}, ${elem.lastTouched})""".
      stripMargin.
      update
}
