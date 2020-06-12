package OKRBParser.infrastructure

import OKRBParser.StreamUtils
import cats.effect.Sync
import org.apache.poi.ss.usermodel.{Row, Sheet}
import scala.jdk.CollectionConverters._

package object parseExcel {

  implicit class SheetOps[F[_] : Sync](sheet: Sheet)(implicit S: StreamUtils[F]) {
    def toStreamIterator: fs2.Stream[F, Row] = {
      S.fromIterator(sheet.rowIterator().asScala)

    }
  }

}
