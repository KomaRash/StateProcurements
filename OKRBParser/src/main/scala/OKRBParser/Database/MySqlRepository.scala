package OKRBParser.Database

import OKRBParser.excelParser.ParseResult
import OKRBParser.OKRBProduct
import cats.data.Validated.{Invalid, Valid}
import cats.effect.Sync
import doobie.implicits._
import cats.instances.list._
import doobie.util.transactor.Transactor
import doobie.util.update.Update
class MySqlRepository[F[_]:Sync](tx:Transactor[F]) extends Repository[F]{
  def save(okrb:ParseResult[List[OKRBProduct]])={
    okrb match {
      case Invalid(e) => e.foreach(println)
      case Valid(a) =>

    }
  }
  override def loadALlOKRB(): fs2.Stream[F, OKRBProduct] = {
    sql"Select * from okrb".
      query[OKRBProduct].
      stream.
      transact(tx)
  }
  def saveList(okrbList:List[OKRBProduct])={
        val sql="insert into okrb (section, class, subcategories, groupings, name) values (?,?,?,?,?)"
        Update[OKRBProduct](sql).updateMany(okrbList).transact(tx)
    }

//  override def saveNewOKRB(OKRB: fs2.Stream[F, OKRBProduct]): F[Int] = ???
}
