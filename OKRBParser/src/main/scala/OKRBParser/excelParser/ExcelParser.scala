package OKRBParser.excelParser

import OKRBParser.excelParser.Algebra.ParseErrorAlgebra
import OKRBParser.{OKRBProduct, StreamUtils}
import cats.effect.{ConcurrentEffect, Sync}
import org.apache.poi.ss.usermodel.{Row, Workbook}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.http4s.multipart.Part


class ExcelParser[F[_]:Sync:ConcurrentEffect](errorAlgeba: ParseErrorAlgebra[F])
                                             (implicit S:StreamUtils[F]) extends Parser [F]{
  override  def giveDocument(part: Part[F]): fs2.Stream[F,Workbook] = {
    for{
      ioStream<-part.body.through(fs2.io.toInputStream)
      myExcelBook =new XSSFWorkbook(ioStream)
      streamDoc<-S.evalF(myExcelBook)
    }yield streamDoc
  }

  override def getStreamSheet(sheetName:String)
                             (document: fs2.Stream[F, Workbook]):fs2.Stream[F, Row] ={
    for{
      doc<-document
      sheet= doc.getSheet(sheetName)
      row<-sheet.toStreamIterator

    }  yield row
  }
  override def getOKRBProducts(rows:fs2.Stream[F,Row]):fs2.Stream[F,OKRBProduct]=for{
    row<-rows
    product<-errorAlgeba.validValue(row.toOKRBProduct)
  }yield product

}
