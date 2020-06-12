package OKRBParser

import cats.data.Validated
import cats.effect.Sync
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.either._
import org.apache.poi.ss.usermodel.{Row, Sheet}
import scala.jdk.CollectionConverters._

package object excelParser  {
  import excelParser.ExcelRowParser._
  type ErrorList=List[String]
  type FastExcelParseResult[A]=Either[ErrorList, A]
  type ParseResult[A]=Validated[ErrorList, A]
  implicit class RowOps(row:Row){
   def toOKRBProduct: ParseResult[OKRBProduct] =(
     parseIntValue(0)(row).toValidated,
     parseIntValue(1)(row).toValidated,
     parseIntValue(2)(row).toValidated,
     parseIntValue(3)(row).toValidated,
     parseStringValue(5)(row).toValidated)
     .mapN(OKRBProduct.apply)
  }
  implicit class SheetOps[F[_]:Sync](sheet:Sheet)(implicit S:StreamUtils[F]){
    def toStreamIterator: fs2.Stream[F, Row] ={
      S.fromIterator(sheet.rowIterator().asScala)

    }
  }

}
