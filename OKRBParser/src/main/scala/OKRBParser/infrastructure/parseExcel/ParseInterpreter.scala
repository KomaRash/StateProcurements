package OKRBParser.infrastructure.parseExcel

import OKRBParser.StreamUtils
import OKRBParser.domain.parseExcel.{ParseAlgebra, ParseErrorAlgebra}
import cats.effect.{ConcurrentEffect, Sync}
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Row, Workbook}
import org.http4s.multipart.Part

abstract class ParseInterpreter[F[_] : Sync : ConcurrentEffect](parseValid: ParseErrorAlgebra[F])
                                                      (implicit S: StreamUtils[F]) extends ParseAlgebra[F] {
  override def giveDocument(part: Part[F]): fs2.Stream[F, Workbook] = {
    for {
      ioStream <- part.body.through(fs2.io.toInputStream)
      myExcelBook = new HSSFWorkbook(ioStream)
      streamDoc <- S.evalF(myExcelBook)
    } yield streamDoc
  }

  def getStreamSheet(sheetName: String)
                             (document: fs2.Stream[F, Workbook]): fs2.Stream[F, Row] = {
    for {
      doc <- document
      _ <- parseValid.isSheetExist(sheetName)(doc)
      sheet = doc.getSheet(sheetName)
      row <- sheet.toStreamIterator
    } yield row
  }
}
