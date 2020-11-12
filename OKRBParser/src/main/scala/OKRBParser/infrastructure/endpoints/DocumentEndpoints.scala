package OKRBParser.infrastructure.endpoints

import tsec.authentication._
import OKRBParser.ParseError
import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.document.{DocumentInfo, DocumentService}
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.EntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart.Part
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.{EntityDecoder, HttpRoutes, ApiVersion => _}

class DocumentEndpoints[F[_] : ConcurrentEffect : Monad](service: DocumentService[F],
                                                         authService: AuthService[F])
  extends Http4sDsl[F] {
  lazy implicit val DocumentInfoDecoder: EntityDecoder[F, List[DocumentInfo]] = jsonOf[F, List[DocumentInfo]]

  private def getDocumentInfo:AuthEndpoint[F,Token]={
    case GET->Root/"purchases"/id asAuthed user=>{
      service.documentInfo(id.toIntOption,user).flatMap(Ok(_))
    }
  }
  def endpoints(): HttpRoutes[F] ={
    authService.auth.liftService(Auth.allRoles(getDocumentInfo))
  }

}
object DocumentEndpoints{
  def endpoints[F[_]:ConcurrentEffect:Monad](service: DocumentService[F],
                                             authService: AuthService[F])={
    new DocumentEndpoints[F](service,authService).endpoints()
  }
}
