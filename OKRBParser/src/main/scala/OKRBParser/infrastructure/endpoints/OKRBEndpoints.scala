package OKRBParser.infrastructure.endpoints

import OKRBParser.ParseError
import OKRBParser.domain.auth.AuthService
import OKRBParser.domain.okrb.{OKRBProduct, OKRBService}
import OKRBParser.infrastructure.endpoints.Pagination.{OptionalPageMatcher, OptionalPageSizeMatcher, OptionalSearchMatcher}
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.EntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart.Part
import org.http4s.{EntityDecoder, Header, Headers, HttpRoutes, ApiVersion => _}

class OKRBEndpoints[F[_] : ConcurrentEffect : Monad](service: OKRBService[F]) extends Http4sDsl[F] {
  lazy implicit val okrbListEncoder: EntityDecoder[F, List[OKRBProduct]] = jsonOf[F, List[OKRBProduct]]

  private def okrbParseEndpoint: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req@POST -> Root  => req.decodeWith(multipart[F], strict = true) {
        request =>
          request.parts.find(filterFileTypes) match {
            case Some(value) => Ok(service.insertOKRB(value).compile.toList.map(_.toString()))
            case None => Ok("не эксель файл")
          }

      }.handleErrorWith {
        case ParseError(list) => BadRequest(list.toString())
      }
    }

  private def getOkrbList: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root  :? OptionalPageMatcher(page):?
      OptionalPageSizeMatcher(pageSize) :?
      OptionalSearchMatcher(searchField) =>
      service.getOKRB(page.getOrElse(0),pageSize.getOrElse(5),searchField.getOrElse("")).flatMap(list => Ok(list))
    case GET -> Root/"length":?OptionalSearchMatcher(search)=>
      service.getLength(search.getOrElse("")).flatMap(len => Ok(len))
  }

  def filterFileTypes(part: Part[F]): Boolean =
    part.headers.toList.exists(_.value.contains(s".xls"))


  def endpoints(): HttpRoutes[F] = okrbParseEndpoint <+> getOkrbList

}

object OKRBEndpoints {
  def endpoints[F[_] : ConcurrentEffect : Monad](service: OKRBService[F],
                                                 auth: AuthService[F]): HttpRoutes[F] = {
    new OKRBEndpoints[F](service).endpoints()
  }
}
