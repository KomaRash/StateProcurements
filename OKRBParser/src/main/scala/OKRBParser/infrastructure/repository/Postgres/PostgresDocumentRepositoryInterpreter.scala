package OKRBParser.infrastructure.repository.Postgres

import OKRBParser.domain.document.{Document, DocumentRepositoryAlgebra}
import doobie.util.transactor.Transactor

class PostgresDocumentRepositoryInterpreter[F[_]](tx:Transactor[F]) extends DocumentRepositoryAlgebra[F]{
  override def load(documentId: Option[Int]): F[Document] = ???

  override def upload(document: Document): F[Document] = ???
}
object DocumentSQL{

}
