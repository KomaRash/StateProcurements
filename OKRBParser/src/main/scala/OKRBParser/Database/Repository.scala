package OKRBParser.Database

import OKRBParser.OKRBProduct
import fs2._

trait Repository[F[_]] {
  def saveOKRBList(dataChunk:Chunk[OKRBProduct]):F[Int]
  def getOKRBList:Stream[F,OKRBProduct]

}
