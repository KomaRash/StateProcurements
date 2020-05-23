package OKRBParser.ExcelParser

import org.apache.poi.ss.usermodel.{Cell, Row}
trait RowParser {
  import cats.syntax.either._
  def parseValue[A](parse:Cell=>A)(numberOfCell:Int)(row: Row): ExcelParseResult[A] = {
    Either.catchNonFatal {
      val cell = row.getCell(numberOfCell)
      parse(cell)
    }.leftMap(_ => List(s"невозможно распарсить ячейку   номер $numberOfCell в строке ${row.getRowNum}"))
  }
  def parseIntValue: Int => Row => ExcelParseResult[Int] ={
    parseValue(cell=>{cell.getStringCellValue.toInt})
  }
  def parseStringValue: Int => Row => ExcelParseResult[String] ={
    parseValue(cell=>cell.getStringCellValue)
  }~

}
