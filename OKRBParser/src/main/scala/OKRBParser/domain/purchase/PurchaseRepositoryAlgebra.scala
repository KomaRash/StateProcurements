package OKRBParser.domain.purchase

import OKRBParser.domain.position.{PositionId, UserId}
import cats.data.OptionT

trait PurchaseRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F] {
  def updateLotInfo(id: Option[PurchaseId], lot: PurchaseLot): F[PurchaseLot]

  def getStatus(id: PurchaseId): F[Option[PurchaseStatus]]

  def getPurchase(id: PurchaseId): F[Option[Purchase]]

  def getPurchaseWithLots(id: PurchaseId): F[Option[Purchase]]

  def setStatus(purchaseStatus: PurchaseStatus, purchaseId: PurchaseId): F[Option[Purchase]]

  def getPurchase(description: String): F[Option[Purchase]]

  def createPurchase(purchase: Purchase, userId: Option[PositionId]): F[Option[Purchase]]

  def addLots(purchaseId: Int, purchaseLot: List[PurchaseLot]): F[Option[Purchase]]

  def findByInfoAndDescription(description: String, purchaseInfo: PurchaseInfo): OptionT[F, Purchase]

  def getPurchaseLots(id: PurchaseId): F[List[PurchaseLot]]

  def getPurchaseList(userId: UserId): F[List[Purchase]]
}
