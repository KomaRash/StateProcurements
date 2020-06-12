package OKRBParser.excelParser

import OKRBParser.OKRBProduct
import org.apache.poi.ss.usermodel.{Row, Workbook}
import org.http4s.multipart.Part

trait ParseAlgebra[F[_]]{
 def giveDocument(part:Part[F]): fs2.Stream[F,Workbook]

 def getStreamSheet(sheetName:String)
                   (document: fs2.Stream[F, Workbook]):fs2.Stream[F, Row]

 def getOKRBProducts(document:fs2.Stream[F,Row]):fs2.Stream[F,OKRBProduct]
}
