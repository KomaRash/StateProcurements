package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.okrb.{OKRBProduct, OKRBRepositoryAlgebra}
import cats.effect.Sync
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.update.Update
import fs2.Chunk

class PostgresOKRBRepositoryInterpreter[F[_] : Sync](tx: Transactor[F],
                                                     maxThreadPool: Int)
  extends OKRBRepositoryAlgebra[F] {

  import OKRBSQL._
  import Pagination._

  override def getOKRBList(pageSize: Int, page: Int, searchField: String): F[List[OKRBProduct]] = {
    offset(pageSize * page, page)(selectByName(searchField)).
      to[List].transact(tx)
  }

  override def insertOKRBChunk(dataChunk: Chunk[OKRBProduct]): F[Int] = {
    val sqlUpdate =
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

  override def getLength(str: String): F[Option[Int]] = rowCountByName(str).option.transact(tx)
}

object OKRBSQL {
  def rowCountByName(name:String): doobie.Query0[Int] ={
    sql"""Select Count(*) from okrb
         |where name like ${"%"+name+"%"} """.
      stripMargin.
      query[Int]
  }
  def selectByName(name: String) =
    sql"""Select section,class
         |,subcategories,groupings,
         |name,productid from okrb
         |where name like ${"%"+name+"%"} """.stripMargin.query[OKRBProduct]
}


