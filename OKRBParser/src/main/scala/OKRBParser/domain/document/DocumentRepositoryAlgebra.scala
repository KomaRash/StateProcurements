package OKRBParser.domain.document

import OKRBParser.domain.purchase.PurchaseId

trait DocumentRepositoryAlgebra[F[_]] {
  def purchaseDocumentsInfo(purchaseId: PurchaseId): F[List[DocumentInfo]]

  def saveDocument(document: Document): F[DocumentInfo]

  def downloadDocument(documentLink: String): F[Document]
}
