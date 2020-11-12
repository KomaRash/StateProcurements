package OKRBParser.domain.document

import OKRBParser.domain.purchase.PurchaseId

case class DocumentInfo(documentName: String,
                        extensions:String,
                        sourceLink:String,
                        description:String="",
                        ) {

}
case class Document(documentInfo: DocumentInfo,
                    body:Array[Byte]=Array(),
                    documentId: Option[Int] = None,
                    purchaseId: Option[PurchaseId]=None)
