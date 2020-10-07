package OKRBParser.infrastructure.repository.Postgres

import java.util.Date

import OKRBParser.domain.purchase._
import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.update.Update
import doobie.util.{Read, Write}
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
class PostgresPurchaseRepositoryInterpreter[F[_]:Sync](tx:Transactor[F],
                                                       maxThreadPool:Int) extends PurchaseRepositoryAlgebra[F] {
  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
  implicit val writePurchaseLots:Write[(PurchaseLot,Int)]=Write[(Float,Date,String,Option[Int],Int)].contramap(lot=>(lot._1.amount,lot._1.deadline.toDate ,lot._1.name,lot._1.okrb.okrbId,lot._2))
  implicit val readPurchaseWithoutLots:Read[Purchase]=
    Read[(Int,String,java.util.Date,Int,String,String)].map{
      p=>
      Purchase(
        PurchaseInfo(
          new DateTime(p._3),p._4,p._5),
        p._2,PurchaseStatus.withName(p._6),
        List(),
        p._1.some)
    }
  override def getById(id: PurchaseId): F[Option[Purchase]] ={
    sql"""SELECT * FROM purchase where purchaseid=${id}"""
      .query[Purchase]
      .option.transact(tx)
  }

  override def getByDescription(description: String): F[Option[Purchase]] = ???

  override def createPurchase(purchase: Purchase): F[Purchase] = {
    sql"""insert into purchase(description, dateofpurchase,positionid, procedurename,status) VALUES (
         |${purchase.description},
         |${purchase.purchaseInfo.dateOfPurchase.toDate},
         |${purchase.purchaseInfo.positionId},
         |${purchase.purchaseInfo.procedureName},
         |${purchase.purchaseStatus.toString})""".
      stripMargin.
      update.
      run.
      map(id=>purchase.copy(purchaseId = id.some)).
      transact(tx)

  }
  override def addLots(purchaseId: Int, purchaseLots: List[PurchaseLot]): F[Purchase] = {

    val sql=s"""insert into purchaselot ( lotamount,deadline, lotname,productid,purchaseid) values (?,?,?,?,?);"""
    Update[(PurchaseLot,Int)](sql).updateMany(purchaseLots.map((_,purchaseId))).transact(tx).flatMap {
     x=>
    sql"""SELECT * FROM purchase where purchaseid=${purchaseId}""".query[Purchase].option
        .transact(tx).map(_.get)
    }
  }

  override def getPurchaseList: F[List[Purchase]] = ???

  override def findByInfoAndDescription(description: String, purchaseInfo: PurchaseInfo): OptionT[F, Purchase] = {
    OptionT (
      sql"""Select * from purchase where description=${description} and positionid=${purchaseInfo.positionId} """.
        query[Purchase].option.transact(tx)
    )
  }
  override def maxThreadPool(): Int = ???
}
