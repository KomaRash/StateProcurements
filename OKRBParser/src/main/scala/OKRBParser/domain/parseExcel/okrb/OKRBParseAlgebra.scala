package OKRBParser.domain.parseExcel.okrb

import OKRBParser.domain.parseExcel.ParseAlgebra
import org.apache.poi.ss.usermodel.Row

trait OKRBParseAlgebra[F[_]] extends ParseAlgebra[F] {
  def convectRow(row: Row): fs2.Stream[F, OKRBProduct]

}
