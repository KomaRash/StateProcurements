package OKRBParser.domain.parseExcel

import org.apache.poi.ss.usermodel.{Row, Workbook}
import org.http4s.multipart.Part

trait ParseAlgebra[F[_]]{
 def giveDocument(part:Part[F]): fs2.Stream[F,Workbook]

 def getStreamSheet(document: fs2.Stream[F, Workbook]):fs2.Stream[F, Row]

}
