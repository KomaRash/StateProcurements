package OKRBParser.infrastructure.repository.fileSystem

import java.io.File
import java.nio.file.{Path, Paths}

import OKRBParser.StreamUtils
import OKRBParser.domain.document.{Document, DocumentFileSystemAlgebra}
import cats.Monad
import cats.data.OptionT
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Sync}
import cats.implicits._
import org.http4s.{Request, Response, StaticFile}

class DocumentFileSystemInterpreter[F[_] : ConcurrentEffect : ContextShift : Sync]( blocker: Blocker)
  extends DocumentFileSystemAlgebra[F] {
  implicit val S=StreamUtils.syncInstance[F]
  import fileSystem._

  override def saveDocument(doc: Document): fs2.Stream[F, Unit] =
    for {
      extensions <- S.evalF(doc.documentInfo.extensions)
      fileLink <- S.evalF(doc.documentInfo.sourceLink)
      path <- S.evalF(Paths.get(s"$home/$fileLink.$extensions"))
      _ <- saveFile(doc, path, blocker)
    } yield ()

  override def getDocument(documentLink: String,request: Request[F])(implicit F: Monad[F]): OptionT[F, Response[F]] = for {
    file <- OptionT(F.pure(getFile(documentLink)))
    source<-StaticFile.fromFile(file,blocker,Some(request))
  } yield source

}

object fileSystem {
  def home = "/home/komarash/IdeaProjects/StateProcurements/dir"

  def saveFile[F[_] : ConcurrentEffect : ContextShift](doc: Document, path: Path, blocker: Blocker)
                                                      (implicit S: StreamUtils[F]): fs2.Stream[F, Unit] =
    S.emits(doc.body.toArray)
      .through(fs2.io.file.writeAll(path, blocker))

  def getFile(documentLink: String): Option[File] = {
    new File(home).listFiles().find(file => file.getName.startsWith(documentLink))
  }

}
