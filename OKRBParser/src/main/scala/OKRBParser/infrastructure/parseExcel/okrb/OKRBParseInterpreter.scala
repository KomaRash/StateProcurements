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
  override def convectRow(row: Row): fs2.Stream[F, OKRBProduct] = {
    for {
      parseResult <- S.evalF(row.toOKRBProduct)
      product <- parseValid.getParseResult(parseResult)
    } yield product
  }

  override def getStreamSheet(document: Workbook): fs2.Stream[F, Row] = {
    getStreamSheet("ОКРБ")(1)(document)
  }
}
