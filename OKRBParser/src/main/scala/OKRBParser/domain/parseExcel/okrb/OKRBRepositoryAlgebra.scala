package OKRBParser.domain.parseExcel.okrb

import OKRBParser.domain.parseExcel.RepositoryAlgebra
import fs2.{Chunk, Stream}

trait OKRBRepositoryAlgebra[F[_]] extends RepositoryAlgebra[F] {
  def getLength(str: String): F[Option[Int]]

  def getOKRBList(pageSize: Int, page: Int, searchField: String): F[List[OKRBProduct]]

  def insertOKRBChunk(dataChunk: Chunk[OKRBProduct]): F[Int]


  def clearOKRBList(): F[Int]
}
