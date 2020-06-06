package OKRBParser.Service

import OKRBParser.Database.Repository
import OKRBParser.ParseError
import OKRBParser.excelParser.Parser
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.EntityDecoder.multipart
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, ApiVersion => _}
class OKRBService[F[_]:ConcurrentEffect](parser:Parser[F],
                                         repository: Repository[F]) extends  Http4sDsl[F] {
  val service: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "multipart" =>
      Ok("Send a file (image, sound, etc) via POST Method")

    case req@POST -> Root / "multipart" =>
      req.decodeWith(multipart[F], strict = true) { response =>
      val a=response
        .parts
        .map(parser.giveDocument)
        .map(parser.getStreamSheet("OKRB"))
        .map(parser.getOKRBProducts)
        .map(_.chunkN(100))
        .map{
          x=>
            x.flatMap{
              chunk=>fs2.Stream.eval(repository.saveOKRBList(chunk))
            }
        }
      Ok(a.toString())

      }.handleErrorWith{
        case ParseError(list)=>BadRequest(list.toString())
      }
  }


}
