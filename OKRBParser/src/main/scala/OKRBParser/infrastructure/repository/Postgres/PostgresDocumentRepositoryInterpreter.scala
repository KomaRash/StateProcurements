package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.document.{DocumentInfo, DocumentRepositoryAlgebra}
import OKRBParser.domain.purchase.PurchaseId
import cats.effect.Sync
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.util.fragment

class PostgresDocumentRepositoryInterpreter[F[_]: Sync](tx:Transactor[F]) extends DocumentRepositoryAlgebra[F]{
  import DocumentSQL._
  override def documentInfo(purchaseId: PurchaseId): F[List[DocumentInfo]] = {
    selectById(purchaseId).to[List].transact(tx)
  }
}
object DocumentSQL{
  private def selectBy=sql"""select documentname,extensions,documentlink,descriptions from documents"""
  def selectById(purchaseId: PurchaseId): doobie.Query0[DocumentInfo] =(selectBy++fr""" where purchaseid=${purchaseId}""").query[DocumentInfo]
}
