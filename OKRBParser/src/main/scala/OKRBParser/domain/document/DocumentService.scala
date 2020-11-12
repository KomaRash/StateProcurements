package OKRBParser.domain.document

import OKRBParser.domain.position.User
import cats.Monad
import org.http4s.Response
class DocumentService[F[_]:Monad] (repository:DocumentRepositoryAlgebra[F]
                                   /*,validation:DocumentValidationAlgebra[F]*/) {
  def documentInfo(purchaseId: Option[Int], user: User): F[List[DocumentInfo]] = {
    repository.documentInfo(purchaseId.get)
  }

  /*def uploadDocument(document: Part[F]): F[Document]={
    repository.upload(document)
  }*/
}
