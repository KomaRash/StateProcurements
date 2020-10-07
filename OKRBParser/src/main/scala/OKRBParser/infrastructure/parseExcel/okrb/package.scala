package OKRBParser.infrastructure.parseExcel

import OKRBParser.domain.parseExcel.ParseResult
import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.infrastructure.parseExcel.ExcelRowParser._
import cats.data.Validated
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.either._
import org.apache.poi.ss.usermodel.Row
package object okrb {
  implicit class RowOps(row:Row){
    def toOKRBProduct: ParseResult[OKRBProduct] =(
      parseIntValue(0)(row).toValidated,
      parseIntValue(1)(row).toValidated,
      parseIntValue(2)(row).toValidated,
      parseIntValue(3)(row).toValidated,
      parseStringValue(5)(row).toValidated,
      Right(None).toValidated)
      .mapN(OKRBProduct.apply)
  }

}