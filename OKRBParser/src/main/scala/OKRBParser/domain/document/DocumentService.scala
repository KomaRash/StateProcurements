package OKRBParser.domain.document

import cats.Monad
import org.http4s.multipart.Part

class DocumentService[F[_]:Monad] (repository:DocumentRepositoryAlgebra[F]) {
  /*def uploadDocument(document: Part[F]): F[Document]={
    repository.upload(document)
  }*/
  def loadDocument(documentId:Option[Int]):F[Document]={
    repository.load(documentId)
  }
}
