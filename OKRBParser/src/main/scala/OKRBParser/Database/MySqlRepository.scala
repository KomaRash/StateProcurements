package OKRBParser.Database
import OKRBParser.OKRBProduct
import cats.effect.Sync
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.update.Update
import fs2.Chunk
class MySqlRepository[F[_]:Sync](tx:Transactor[F]) extends Repository[F]{

  override def getOKRBList: fs2.Stream[F, OKRBProduct] = {
    sql"""Select * from okrb"""
      .query[OKRBProduct]
      .stream
      .transact(tx)
  }

  override def saveOKRBList(dataChunk: Chunk[OKRBProduct]): F[Int] = {
    val sqlUpdate=
      sql"""insert into okrb
           |(section, class, subcategories, groupings, name)
           |values (?,?,?,?,?)""".stripMargin
    Update[OKRBProduct](sqlUpdate.toString)
      .updateMany(dataChunk)
      .transact(tx)
  }
}
