package OKRBParser.domain.okrb

import fs2.Chunk

trait OKRBRepositoryAlgebra[F[_]] {
  def getLength(str: String): F[Option[Int]]

  def getOKRBList(pageSize: Int, page: Int, searchField: String): F[List[OKRBProduct]]

  def insertOKRBChunk(dataChunk: Chunk[OKRBProduct]): F[Int]


  def clearOKRBList(): F[Int]
}
