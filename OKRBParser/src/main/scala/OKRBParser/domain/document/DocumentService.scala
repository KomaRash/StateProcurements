package OKRBParser.domain.document

import java.io.File
import java.util.UUID.randomUUID

import OKRBParser.domain.position.User
import cats.Monad
import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import fs2.Stream
import org.http4s.{Request, Response, StaticFile}
import scodec.bits.ByteVector

class DocumentService[F[_] : Sync : Monad](repository: DocumentRepositoryAlgebra[F],
                                           fileSystem: DocumentFileSystemAlgebra[F]
                                           /*,validation:DocumentValidationAlgebra[F]*/) {
  def documentInfo(purchaseId: Option[Int], user: User): F[List[DocumentInfo]] = {
    repository.purchaseDocumentsInfo(purchaseId.get)
  }

  def uploadDocument(document: Stream[F, Byte], name: String,
                     description: String, purchaseId: Option[Int],
                     user: User)(implicit F: Monad[F]): F[DocumentInfo] = for {
    name <- F.pure(name.split('.').toList)
    body <- document.compile.to(ByteVector)
    link <- F.pure(randomUUID().toString)
    doc <- F.pure(Document(
      DocumentInfo(
        name.head, name.lastOption.getOrElse(""),
        link, description
      ),
      body, purchaseId
    ))
    _ <- fileSystem.saveDocument(doc).compile.toVector
    result <- repository.saveDocument(doc)
  } yield result

  def downloadDocument: (String, Request[F]) => OptionT[F, Response[F]] = fileSystem.getDocument

}
