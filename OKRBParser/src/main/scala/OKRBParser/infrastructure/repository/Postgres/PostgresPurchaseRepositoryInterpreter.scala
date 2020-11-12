package OKRBParser.infrastructure.repository.Postgres

import java.util.Date

import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{PositionId, UserId}
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

object PurchaseSql {
  implicit val writePurchaseLots: Write[(PurchaseLot, Int)] = Write[(Float, Date, String, Option[Int], Int)].contramap(lot => (lot._1.amount, lot._1.deadline.toDate, lot._1.name, lot._1.okrb.okrbId, lot._2))
  implicit val readPurchaseWithoutLots: Read[Purchase] =
    Read[(Int, String, java.util.Date, Int, String, String)].map {
      p =>
        Purchase(
          PurchaseInfo(
            new DateTime(p._3), p._4, p._5),
          p._2, PurchaseStatus.withName(p._6),
          List(),
          p._1.some)
    }
  implicit val readPurchaseLot: Read[PurchaseLot] = Read[(Int, Date, Float, String, Int, Int, Int, Int, Int, String)].map {
    l =>
      PurchaseLot(OKRBProduct(l._6, l._7,
        l._8, l._9,
        l._10, l._5.some),
        new DateTime(l._2), l._3, l._4, l._1.some)
  }

  def selectList(userId: UserId): doobie.Query0[Purchase] = {
    sql"""Select * from purchase where positionid=(select positionid from users where userid=$userId)""".
      query[Purchase]
  }

  def selectById(id: PurchaseId): doobie.Query0[Purchase] = {
    sql"""SELECT * FROM purchase where purchaseid=${id}""".
      query[Purchase]
  }

  def selectByDescription(description: String): doobie.Query0[Purchase] = {
    sql"""SELECT * FROM purchase where description=${description}""".
      query[Purchase]
  }

  def selectPurchaseLots(purchaseId: PurchaseId): doobie.Query0[PurchaseLot] = {
    sql"""select  p.purchaselotid,p.deadline,p.lotamount,
         |p.lotname,o.productid, section,
         | class, subcategories, groupings,
         |  name from purchaselot as p
         |  natural join okrb as o
         |  where purchaseid=$purchaseId""".stripMargin.
      query[PurchaseLot]
  }

  def insertPurchase(purchase: Purchase, positionId: Option[PositionId]): doobie.Update0 = {
    sql"""insert into purchase (description, dateofpurchase,positionid, procedurename,status) VALUES (
         |${purchase.description},
         |${purchase.purchaseInfo.dateOfPurchase.toDate},
         |$positionId,
         |${purchase.purchaseInfo.procedureName},
         |${purchase.purchaseStatus.toString})""".
      stripMargin.
      update
  }

  def insertLots(purchaseLots: List[PurchaseLot], purchaseId: PurchaseId): doobie.ConnectionIO[PurchaseId] = {
    val sql = s"""insert into purchaselot ( lotamount,deadline, lotname,productid,purchaseid) values (?,?,?,?,?);"""
    Update[(PurchaseLot, Int)](sql).updateMany(purchaseLots.map((_, purchaseId)))
  }

  def updateStatus(purchaseStatus: PurchaseStatus, purchaseId: PurchaseId): doobie.Update0 = {
    sql"""update purchase set status=${purchaseStatus.toString()} where purchaseid=$purchaseId""".update
  }

  def updateLot(id: Option[PurchaseId], lot: PurchaseLot): doobie.Update0 = {
    sql"""update purchaseLot set lotamount=${lot.amount},
         |deadline=${lot.deadline.toDate},
         |productid=${lot.okrb.okrbId},
         |lotname=${lot.name} where purchaselotid=$id""".stripMargin.update
  }
}

class PostgresPurchaseRepositoryInterpreter[F[_] : Sync](tx: Transactor[F])
  extends PurchaseRepositoryAlgebra[F] {

  import PurchaseSql._

  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

  override def getPurchase(id: PurchaseId): F[Option[Purchase]] = {
    selectById(id)
      .option
      .transact(tx)
  }


  override def getPurchase(description: String): F[Option[Purchase]] = {
    selectByDescription(description)
      .option
      .transact(tx)
  }

  override def createPurchase(purchase: Purchase, positionId: Option[PositionId]): F[Option[Purchase]] = {
    for {
      _ <- insertPurchase(purchase, positionId).run.transact(tx)
      purchase <- findByInfoAndDescription(purchase.description, purchase.purchaseInfo).value
    } yield purchase
  }

  override def addLots(purchaseId: Int, purchaseLots: List[PurchaseLot]): F[Option[Purchase]] = {
    for {
      _ <- insertLots(purchaseLots, purchaseId).transact(tx)
      purchase <- getPurchase(purchaseId)
      purchaseLots <- getPurchaseLots(purchaseId)
      p = purchase.map(_.copy(purchaseLots = purchaseLots))
    } yield p

  }

  override def getPurchaseList(userId: UserId): F[List[Purchase]] = {
    selectList(userId).to[List].transact(tx)
  }

  override def findByInfoAndDescription(description: String, purchaseInfo: PurchaseInfo): OptionT[F, Purchase] = {
    OptionT(
      sql"""Select * from purchase where description=${description}  """.
        query[Purchase].option.transact(tx)
    )
  }

  override def maxThreadPool(): Int = ???

  override def setStatus(purchaseStatus: PurchaseStatus, purchaseId: PurchaseId): F[Option[Purchase]] = {
    for {
      _ <- updateStatus(purchaseStatus, purchaseId).run.transact(tx)
      purchase <- getPurchaseWithLots(purchaseId)
    } yield purchase
  }

  override def getStatus(id: PurchaseId): F[Option[PurchaseStatus]] = {
    selectById(id).
      option.
      transact(tx).
      map(_.map(_.purchaseStatus))
  }

  override def getPurchaseLots(id: PurchaseId): F[List[PurchaseLot]] = {
    selectPurchaseLots(id).
      to[List].
      transact(tx)
  }

  override def getPurchaseWithLots(id: PurchaseId): F[Option[Purchase]] = {
    for {
      purchase <- getPurchase(id)
      purchaseLots <- getPurchaseLots(id)
      purchaseWithLots = purchase.map(_.copy(purchaseLots = purchaseLots))
    } yield purchaseWithLots
  }

  override def updateLotInfo(id: Option[PurchaseId], lot: PurchaseLot): F[PurchaseLot] = {
    updateLot(id, lot).run.transact(tx).map(_ => lot)
  }
}
