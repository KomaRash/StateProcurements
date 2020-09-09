package OKRBParser.domain.purchase

import OKRBParser.domain.parseExcel.RepositoryAlgebra

trait PurchaseRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F]{
  def createPurchase(purchaseInfo: PurchaseInfo)
  def addLot(purchaseId:Int,purchaseLot: PurchaseLot)
  def getPurchaseList:fs2.Stream[F,PurchaseInfo]
}
