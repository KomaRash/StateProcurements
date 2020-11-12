package OKRBParser.domain.document

import OKRBParser.domain.purchase.PurchaseId
import cats.effect.Sync
import org.http4s.{Header, Headers}
import org.http4s.multipart.Part
import scodec.bits.ByteVector

case class DocumentInfo(documentName: String,
                        extensions:String,
                        sourceLink:String,
                        descriptions:String="",
                        ) {

}
case class Document(documentInfo: DocumentInfo,
                    body:ByteVector,
                    purchaseId: Option[PurchaseId]=None,
                    documentId: Option[Int] = None){
  def toPart[F[_]:Sync]: Part[F] = Part.fileData(documentInfo.descriptions,
    s"${documentInfo.documentName}.${documentInfo.extensions}",
    fs2.Stream.emits[F,Byte](body.toArray))
}
