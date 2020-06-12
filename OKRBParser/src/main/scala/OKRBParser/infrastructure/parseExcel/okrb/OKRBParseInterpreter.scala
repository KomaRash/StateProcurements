package OKRBParser.infrastructure.parseExcel.okrb

import OKRBParser.StreamUtils
import OKRBParser.domain.parseExcel.okrb.{OKRBParseAlgebra, OKRBProduct}
import OKRBParser.domain.parseExcel.ParseErrorAlgebra
import OKRBParser.infrastructure.parseExcel.ParseInterpreter
import cats.effect.ConcurrentEffect
import org.apache.poi.ss.usermodel.{Row, Workbook}

class OKRBParseInterpreter[F[_] : ConcurrentEffect](parseValid: ParseErrorAlgebra[F])
                                                   (implicit S: StreamUtils[F])
  extends ParseInterpreter[F](parseValid)
    with OKRBParseAlgebra[F] {
  override def convectRow(rows: fs2.Stream[F, Row]): fs2.Stream[F, OKRBProduct] = {
    for{
      dataRow<-rows.drop(1)// notes: add configure with Start data row index
      parseResult<-S.evalF(dataRow.toOKRBProduct)
      product<-parseValid.getParseResult(parseResult)
    }yield product
  }

  override def getStreamSheet(document: fs2.Stream[F, Workbook]): fs2.Stream[F, Row] = {
    getStreamSheet("ОКРБ")(document)
  }
}
