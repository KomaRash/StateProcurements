package OKRBParser

import OKRBParser.OKRBGroups.Product
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.either._
import org.apache.poi.ss.usermodel.Row
package object ExcelParser extends RowParser {
  type ExcelParseResult[A]=Either[List[String], A]
  implicit class ExcelRowParser(row:Row){
   def toProduct: Either[List[String], Product] =(
     parseIntValue(0)(row).toValidated,
     parseIntValue(1)(row).toValidated,
     parseIntValue(2)(row).toValidated,
     parseIntValue(3)(row).toValidated,
     parseStringValue(5)(row).toValidated
   ).mapN(Product.apply).toEither
  }

}
