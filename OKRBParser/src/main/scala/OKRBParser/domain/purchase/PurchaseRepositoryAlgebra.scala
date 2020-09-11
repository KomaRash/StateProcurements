package OKRBParser.domain.purchase

import OKRBParser.domain.parseExcel.RepositoryAlgebra
import cats.data.OptionT

trait PurchaseRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F]{
  def getById(id: PurchaseId):F[Option[Purchase]]
  def getByDescription(description:String):F[Option[Purchase]]
  def createPurchase(purchase: Purchase):F[Purchase]
  def addLots(purchaseId:Int, purchaseLot: List[PurchaseLot]):F[Purchase]
  def getPurchaseList:F[List[Purchase]]
  def findByInfoAndDescription(description:String,purchaseInfo: PurchaseInfo):OptionT[F,Purchase]
}
