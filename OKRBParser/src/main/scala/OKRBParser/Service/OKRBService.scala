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
    case GET -> Root / "okrb" =>
      Ok("Send a file (image, sound, etc) via POST Method")

    case req@POST -> Root / "okrb" =>
      req.decodeWith(multipart[F], strict = true) { request =>
        val a=request
        .parts
        .map(parser.giveDocument)
        .map(parser.getStreamSheet("OKRB"))
        .map(parser.getOKRBProducts)
        .map(_.chunkN(100))
        .map{
          x=>
            x.parEvalMap(10){
              chunk=> repository.saveOKRBList(chunk)
            }
        }.sequence.compile.toList

      Ok(a.map(_.map(_.sum).sum.toString))

      }.handleErrorWith{
        case ParseError(list)=>BadRequest(list.toString())
      }
  }


}
