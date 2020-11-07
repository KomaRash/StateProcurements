package OKRBParser.domain.parseExcel.okrb

import OKRBParser.domain.parseExcel.RepositoryAlgebra
import fs2.{Chunk, Stream}

trait OKRBRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F] {
  def insertOKRBChunk(dataChunk: Chunk[OKRBProduct]): F[Int]

  def getOKRBList():F[List[OKRBProduct]]

  def clearOKRBList(): F[Int]
}
