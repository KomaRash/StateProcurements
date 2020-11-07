package OKRBParser.domain.parseExcel.okrb

import cats.data.EitherT
import cats.effect.Concurrent
import org.http4s.Response
import org.http4s.multipart.Part

class OKRBService[F[_] : Concurrent](repository: OKRBRepositoryAlgebra[F],
                                     okrbParse: OKRBParseAlgebra[F]) {
  def getOKRB(): F[List[OKRBProduct]]= {
    repository.getOKRBList
  }

  def insertOKRB(okrbDocument: Part[F]): fs2.Stream[F, Int] = {
    val okrbProducts = for {
      document <- okrbParse.giveDocument(okrbDocument)
      row <- okrbParse.getStreamSheet(document)
      okrbProduct <- okrbParse.convectRow(row)
    } yield okrbProduct
    okrbProducts // привязать к конфиг файлу
      .chunkN(100) // аналогично
      .parEvalMap(repository.maxThreadPool())(repository.insertOKRBChunk)
  }
}
