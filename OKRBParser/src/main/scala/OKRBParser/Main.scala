

/*

package OKRBParser

import org.apache.poi.ss.usermodel.WorkbookFactory

object Main{
  import scala.jdk.CollectionConverters._
  def main(args: Array[String]): Unit={
    import OKRBParser.ExcelParser._
    import fs2._, cats.effect.IO
    import java.io.File
    val f = new File("C:\\Users\\KOma_rash\\Desktop\\Гос закупки\\OKRB.xlsx")
    val workbook = WorkbookFactory.create(f)
    val sheet = workbook.getSheetAt(0)
    val tableIterator=sheet.iterator().asScala
    val okrbStream=Stream.fromIterator(tableIterator).covary[IO].chunkN(100)

  }

  // For implicit conversions like converting RDDs to DataFrames
}

*/
