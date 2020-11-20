package OKRBParser.infrastructure.endpoints

import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.document.{DocumentInfo, DocumentService}
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.EntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, ApiVersion => _}
import tsec.authentication._

class DocumentEndpoints[F[_] : ConcurrentEffect : Monad](service: DocumentService[F],
                                                         authService: AuthService[F],
                                                        ) extends Http4sDsl[F] {
  lazy implicit val DocumentInfoDecoder: EntityDecoder[F, List[DocumentInfo]] = jsonOf[F, List[DocumentInfo]]

  private def getDocumentInfo: AuthEndpoint[F, Token] = {
    case GET -> Root / "purchases" / id asAuthed user => {
      service.documentInfo(id.toIntOption, user).flatMap(Ok(_))
    }
  }

  private def uploadDocument: AuthEndpoint[F, Token] = {
    case req@POST -> Root / "purchases" / id asAuthed user => for {
      value <- req.request.decodeWith(multipart[F], strict = true) { request =>
        request.parts.map { part =>
          service.uploadDocument(part.body, part.filename.get, part.name.get, id.toIntOption, user)
        }.sequence.flatMap(documents => Ok(documents.toList))
      }
    } yield value

  }

  private def downloadDocument: AuthEndpoint[F, Token] = {
    case req@GET -> Root / link asAuthed user => {
      service.downloadDocument(link, req.request).getOrElseF(NotFound())
    }
  }

  def endpoints(): HttpRoutes[F] = {
    authService.auth.liftService(
      Auth.
        allRoles(uploadDocument
          .orElse(getDocumentInfo)
          .orElse(downloadDocument)
        )
    )

  }
}

object DocumentEndpoints {
  def endpoints[F[_] : ConcurrentEffect : Monad](service: DocumentService[F],
                                                 authService: AuthService[F]) = {
    new DocumentEndpoints[F](service, authService).endpoints()
  }

}
