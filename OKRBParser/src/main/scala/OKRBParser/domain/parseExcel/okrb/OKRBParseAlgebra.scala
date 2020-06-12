package OKRBParser.domain.parseExcel.okrb

import OKRBParser.domain.parseExcel.ParseAlgebra
import org.apache.poi.ss.usermodel.Row

trait OKRBParseAlgebra[F[_]] extends ParseAlgebra[F] {
  def convectRow(rows: fs2.Stream[F, Row]): fs2.Stream[F, OKRBProduct]

}
