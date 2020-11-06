package OKRBParser.infrastructure.endpoints

import OKRBParser.ParseError
import OKRBParser.domain.parseExcel.okrb.OKRBParseService
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.EntityDecoder.multipart
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart.Part
import org.http4s.{HttpRoutes, ApiVersion => _}

class OKRBEndpoints[F[_] : ConcurrentEffect : Monad](okrbService: OKRBParseService[F]) extends Http4sDsl[F] {
  private def okrbParseEndpoint: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req@POST -> Root / "okrb" => req.decodeWith(multipart[F], strict = true) {
        request =>
          request.parts.find(filterFileTypes) match {
            case Some(value) => Ok(okrbService.insertOKRB(value).compile.toList.map(_.toString()))
            case None => Ok("не эксель файл")
          }

      }.handleErrorWith {
        case ParseError(list) => BadRequest(list.toString())
      }
    }

  def filterFileTypes(part: Part[F]): Boolean =
    part.headers.toList.exists(_.value.contains(s".xls"))

  def endpoints(): HttpRoutes[F] = okrbParseEndpoint

}
