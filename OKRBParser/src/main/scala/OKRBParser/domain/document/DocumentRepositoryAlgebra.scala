package OKRBParser.domain.document

trait DocumentRepositoryAlgebra[F[_]] {
  def load(documentId: Option[Int]): F[Document]

  def upload(document: Document): F[Document]

}
