package OKRBParser.domain.parseExcel.okrb

import cats.effect.Concurrent
import org.http4s.multipart.Part

class OKRBParseService[F[_] : Concurrent](okrbRepository: OKRBRepositoryAlgebra[F],
                                          okrbParse: OKRBParseAlgebra[F]) {
  def insertOKRB(okrbDocument: Part[F]): fs2.Stream[F, Int] = {
    val okrbProducts = for {
      document <- okrbParse.giveDocument(okrbDocument)
      row <- okrbParse.getStreamSheet(document)
      okrbProduct <- okrbParse.convectRow(row)
    } yield okrbProduct
    okrbProducts // привязать к конфиг файлу
      .chunkN(100) // аналогично
      .parEvalMap(okrbRepository.maxThreadPool())(okrbRepository.insertOKRBChunk)


  }
}
