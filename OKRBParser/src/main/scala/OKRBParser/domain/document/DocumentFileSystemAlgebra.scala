package OKRBParser.domain.document

import java.io

import cats.Monad
import cats.data.OptionT
import org.http4s.{Request, Response}

import scala.reflect.io.File

trait DocumentFileSystemAlgebra[F[_]] {
  def saveDocument(doc: Document): fs2.Stream[F, Unit]

  def getDocument(documentLink: String, request: Request[F])(implicit F: Monad[F]): OptionT[F, Response[F]]
}
