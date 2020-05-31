package OKRBParser

import cats.data.Validated
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.either._
import org.apache.poi.ss.usermodel.Row
package object excelParser extends ExcelRowParser {
  type ErrorList=List[String]
  type FastExcelParseResult[A]=Either[ErrorList, A]
  type ParseResult[A]=Validated[ErrorList, A]
  implicit class arser(row:Row){
   def toOKRBProduct: ParseResult[OKRBProduct] =(
     parseIntValue(0)(row).toValidated,
     parseIntValue(1)(row).toValidated,
     parseIntValue(2)(row).toValidated,
     parseIntValue(3)(row).toValidated,
     parseStringValue(5)(row).toValidated
   ).mapN(OKRBProduct.apply)
  }

}
