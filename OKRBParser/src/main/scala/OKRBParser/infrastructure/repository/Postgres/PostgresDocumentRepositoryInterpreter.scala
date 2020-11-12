package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.document.{Document, DocumentInfo, DocumentRepositoryAlgebra}
import OKRBParser.domain.purchase.PurchaseId
import cats.effect.Sync
import cats.implicits._
import doobie.implicits._
import doobie.util.Read
import doobie.util.transactor.Transactor
import scodec.bits.ByteVector

class PostgresDocumentRepositoryInterpreter[F[_] : Sync](tx: Transactor[F]) extends DocumentRepositoryAlgebra[F] {

  import DocumentSQL._

  override def purchaseDocumentsInfo(purchaseId: PurchaseId): F[List[DocumentInfo]] = {
    selectById(purchaseId).to[List].transact(tx)
  }

  override def saveDocument(document: Document): F[DocumentInfo] = {
    for {
      doc <- insert(document).run.transact(tx)
    } yield document.documentInfo
  }

  override def downloadDocument(documentLink: String): F[Document] = {
    selectDocument(documentLink).option.transact(tx).map(_.get)
  }
}

object DocumentSQL {
  implicit val readDocument: Read[Document] =
    Read[(Int, String, Array[Byte], String, String, String, Int)].map {
      doc =>
        Document(
          DocumentInfo(doc._2, doc._4, doc._6,
            doc._5),
          ByteVector(doc._3), doc._7.some,
          doc._1.some
        )

    }

  private def selectBy = sql"""select documentname,extensions,documentlink,descriptions from documents"""

  /*
  безумно странный запрос
  def selectByLink(link: String): doobie.Query0[DocumentInfo] = (selectBy ++ fr""" where documentlink='$link'""").query[DocumentInfo]
  */
  def selectById(purchaseId: PurchaseId): doobie.Query0[DocumentInfo] = (selectBy ++ fr""" where purchaseid = ${purchaseId}""").query[DocumentInfo]

  def selectDocument(link: String) = sql"""Select * from documents where documentlink = $link""".query[Document]

  def insert(document: Document): doobie.Update0 =
    sql"""insert into documents(documentname, documentbody, extensions, descriptions, documentlink, purchaseid)
         | values(
         | ${document.documentInfo.documentName},${document.body.toArray},
         | ${document.documentInfo.extensions},${document.documentInfo.descriptions},
         | ${document.documentInfo.sourceLink},${document.purchaseId.get})""".stripMargin.update
}
