package OKRBParser.domain.parseExcel

import fs2.Stream
import org.apache.poi.ss.usermodel.Workbook

trait ParseErrorAlgebra[F[_]] {
  def getParseResult[A](parseResult: ParseResult[A]):Stream[F,A]
  def isSheetExist(sheetName:String)(workbook: Workbook):Stream[F,Unit]
}
