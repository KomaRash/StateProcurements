package OKRBParser.domain.okrb

import cats.data.EitherT
import cats.effect.Concurrent
import org.http4s.Response
import org.http4s.multipart.Part

class OKRBService[F[_] : Concurrent](repository: OKRBRepositoryAlgebra[F],
                                     okrbParse: OKRBParseAlgebra[F]) {
  def getLength(str: String):F[Option[Int]] ={
    repository.getLength(str)
  }

  def getOKRB(pageSize:Int,page:Int,searchField:String): F[List[OKRBProduct]]= {
    repository.getOKRBList(pageSize,page,searchField)
  }

  def insertOKRB(okrbDocument: Part[F]): fs2.Stream[F, Int] = {
    val okrbProducts = for {
      document <- okrbParse.giveDocument(okrbDocument)
      row <- okrbParse.getStreamSheet(document)
      okrbProduct <- okrbParse.convectRow(row)
    } yield okrbProduct
    okrbProducts // привязать к конфиг файлу
      .chunkN(100) // аналогично
      .parEvalMap(3)(repository.insertOKRBChunk)
  }
}
