package OKRBParser.domain.document

import OKRBParser.domain.purchase.PurchaseId

trait DocumentRepositoryAlgebra[F[_]] {
  def documentInfo(purchaseId: PurchaseId):F[List[DocumentInfo]]
}
