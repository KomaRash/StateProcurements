package OKRBParser.Database

import OKRBParser.OKRBProduct
import fs2.Stream
trait Repository[F[_]] {
  def saveNewOKRB(OKRB:Stream[F,OKRBProduct]):F[Int] = ???
  def loadALlOKRB():Stream[F,OKRBProduct]
}
