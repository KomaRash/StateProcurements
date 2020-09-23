package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.purchase.{Purchase, PurchaseId, PurchaseInfo, PurchaseLot, PurchaseRepositoryAlgebra, PurchaseStatus}
import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.util.Read
class PostgresPurchaseRepositoryInterpreter[F[_]:Sync](tx:Transactor[F],
                                                       maxThreadPool:Int) extends PurchaseRepositoryAlgebra[F] {
  implicit val readPurchaseWithoutLots:Read[Purchase]=Read[(Int,String,java.util.Date,Int,String)].map(p=>Purchase(PurchaseInfo(p._4,p._5),p._2,PurchaseStatus.Execution,List(),p._1.some))
  override def getById(id: PurchaseId): F[Option[Purchase]] = ???/* {
    sql"""SELECT * FROM purchase where purchaseid=${id}"""
      .query[Purchase]
      .option.transact(tx)
  }*/

  override def getByDescription(description: String): F[Option[Purchase]] = ???

  override def createPurchase(purchase: Purchase): F[Purchase] = {
    sql"""insert into purchase(description, dateofpurchase,positionid, procedurename) VALUES (
         |${purchase.description},
         |now(),
         |${purchase.purchaseInfo.positionId},
         |${purchase.purchaseInfo.procedureName})""".
      stripMargin.
      update.
      run.
      map(id=>purchase.copy(purchaseId = id.some)).
      transact(tx)
  }
  override def addLots(purchaseId: Int, purchaseLot: List[PurchaseLot]): F[Purchase] = ???

  override def getPurchaseList: F[List[Purchase]] = ???

  override def findByInfoAndDescription(description: String, purchaseInfo: PurchaseInfo): OptionT[F, Purchase] = {
    OptionT (
      sql"""Select * from purchase where description=${description} and positionid=${purchaseInfo.positionId} and """.
        query[Purchase].option.transact(tx)
    )
  }
  override def maxThreadPool(): Int = ???
}
