package OKRBParser.excelParser.Algebra
import OKRBParser.excelParser.ParseResult
import OKRBParser.{ParseError, StreamUtils}
import cats.data.Validated.{Invalid, Valid}
import cats.effect.Sync
import fs2.Stream
import org.apache.poi.ss.usermodel.Workbook
class ExcelParseErrorInterpreter[F[_]:Sync](implicit S:StreamUtils[F]) extends ParseErrorAlgebra [F] {
  override def getParseResult[A](parseResult: ParseResult[A]): Stream[F, A] =
    parseResult match {
      case Invalid(errorList) => S.error[A](ParseError(errorList))
      case Valid(value) => S.evalF(value)
    }

  override def isSheetExist(sheetName: String)(workbook: Workbook): Stream[F, Unit] = {
    Option(workbook.getSheet(sheetName)) match {
      case Some(_) => S.evalF()
      case None =>S.error(ParseError(List(s"не найден лист $sheetName")))
    }
  }
}