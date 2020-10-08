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
object PurchaseSql{
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
  def selectList={
    sql"""Select * from purchase""".
      query[Purchase]
  }
  def selectById(id: PurchaseId): doobie.Query0[Purchase] ={
    sql"""SELECT * FROM purchase where purchaseid=${id}""".
      query[Purchase]
  }
  def selectByDescription(description: String) : doobie.Query0[Purchase] ={
    sql"""SELECT * FROM purchase where description=${description}""".
      query[Purchase]
  }
  def insertPurchase(purchase: Purchase): doobie.Update0 ={
    sql"""insert into purchase(description, dateofpurchase,positionid, procedurename,status) VALUES (
         |${purchase.description},
         |${purchase.purchaseInfo.dateOfPurchase.toDate},
         |${purchase.purchaseInfo.positionId},
         |${purchase.purchaseInfo.procedureName},
         |${purchase.purchaseStatus.toString})""".
      stripMargin.
      update
  }
  def insertLots(purchaseLots:List[PurchaseLot],purchaseId: PurchaseId): doobie.ConnectionIO[PurchaseId] ={
    val sql=s"""insert into purchaselot ( lotamount,deadline, lotname,productid,purchaseid) values (?,?,?,?,?);"""
    Update[(PurchaseLot,Int)](sql).updateMany(purchaseLots.map((_,purchaseId)))
  }
  def updateStatus(purchaseStatus: PurchaseStatus, purchaseId: PurchaseId): doobie.Query0[Purchase] ={
    sql"""update purchase set status=${purchaseStatus.toString()} where purchaseid=$purchaseId""".query[Purchase]
  }



}
class PostgresPurchaseRepositoryInterpreter[F[_]:Sync](tx:Transactor[F],
                                                       maxThreadPool:Int) extends PurchaseRepositoryAlgebra[F] {
  import PurchaseSql._
  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
  override def getPurchase(id: PurchaseId): F[Option[Purchase]] ={
    selectById(id)
      .option
      .transact(tx)
  }


  override def getPurchase(description: String): F[Option[Purchase]] = {
    selectByDescription(description)
      .option
      .transact(tx)
  }

  override def createPurchase(purchase: Purchase): F[Purchase] = {
       insertPurchase(purchase).
       run.
       map(id=>purchase.copy(purchaseId = id.some)).
       transact(tx)

  }
  override def addLots(purchaseId: Int, purchaseLots: List[PurchaseLot]): F[Purchase] = {
    insertLots(purchaseLots,purchaseId)
      .transact(tx)
      .flatMap {
        _ =>
          selectById(purchaseId)
            .option
            .transact(tx)
            .map(_.get.copy(purchaseLots=purchaseLots))
      }
  }

  override def getPurchaseList: F[List[Purchase]] = {
    selectList.to[List].transact(tx)
  }

  override def findByInfoAndDescription(description: String, purchaseInfo: PurchaseInfo): OptionT[F, Purchase] = {
    OptionT (
      sql"""Select * from purchase where description=${description} and positionid=${purchaseInfo.positionId} """.
        query[Purchase].option.transact(tx)
    )
  }
  override def maxThreadPool(): Int = ???

  override def setStatus(purchaseStatus: PurchaseStatus, purchaseId: PurchaseId): F[Option[Purchase]] = {
    updateStatus(purchaseStatus,purchaseId)
      .option
      .transact(tx)
  }

  override def getStatus(id: PurchaseId): F[Option[PurchaseStatus]] = {
  selectById(id).
    option.
    transact(tx).
    map(_.map(_.purchaseStatus))
  }

  override def getPurchaseWithLots(id: PurchaseId): F[Option[Purchase]] = ???
}
