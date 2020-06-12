package OKRBParser.domain.parseExcel.okrb

import fs2.{Chunk, Stream}

trait OKRBRepositoryAlgebra[F[_]] {
  def saveOKRBList(dataChunk:Chunk[OKRBProduct]):F[Int]
  def getOKRBList:Stream[F,OKRBProduct]
  def clearOKRBList():F[Int]
}
