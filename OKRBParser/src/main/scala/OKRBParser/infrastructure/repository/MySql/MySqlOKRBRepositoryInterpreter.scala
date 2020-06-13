package OKRBParser.infrastructure.repository.MySql

import OKRBParser.domain.parseExcel.okrb.{OKRBProduct, OKRBRepositoryAlgebra}
import cats.effect.Sync
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.update.Update
import fs2.Chunk
class MySqlOKRBRepositoryInterpreter[F[_]:Sync](tx:Transactor[F],
                                                maxThreadPool:Int)
  extends OKRBRepositoryAlgebra[F]{

  override def getOKRBList: fs2.Stream[F, OKRBProduct] = {
    sql"""Select * from okrb"""
      .query[OKRBProduct]
      .stream
      .transact(tx)
  }

  override def insertOKRBChunk(dataChunk: Chunk[OKRBProduct]): F[Int] = {
    val sqlUpdate=
      """insert into okrb
           |(section, class, subcategories, groupings, name)
           |values (?,?,?,?,?)""".stripMargin
    Update[OKRBProduct](sqlUpdate)
      .updateMany(dataChunk)
      .transact(tx)
  }

  override def clearOKRBList(): F[Int] = {
    sql"""DELETE from okrb where true""".update.run.transact(tx)
  }

  override def maxThreadPool(): Int = maxThreadPool
}
