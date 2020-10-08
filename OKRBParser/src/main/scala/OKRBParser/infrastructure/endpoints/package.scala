package OKRBParser.infrastructure

import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User}
import OKRBParser.domain.purchase.{Purchase, PurchaseInfo, PurchaseLot}
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import scala.util.control.NonFatal

package object endpoints {
  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
  lazy implicit val OKRBProductEncoder:Encoder[OKRBProduct]=deriveEncoder[OKRBProduct]
  lazy implicit val OKRBProductDecoder:Decoder[OKRBProduct]=deriveDecoder[OKRBProduct]
  lazy implicit val DateEncoder:Encoder[DateTime]=(a:DateTime)=>a.toString(dateFormatter).asJson
  implicit val decodeDateTime: Decoder[DateTime] = Decoder.decodeString.emap { s =>
    try {
      Right(DateTime.parse(s, dateFormatter))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }
  lazy implicit val PurchaseLotCodec: Codec[PurchaseLot] = deriveCodec
  lazy implicit val PurchaseCodec:Codec[Purchase]=deriveCodec
  lazy implicit val PurchaseDecoder:Decoder[Purchase]=deriveDecoder
  lazy implicit val PurchaseInfoCodec:Codec[PurchaseInfo]=deriveCodec
  lazy implicit val PositionCodec:Codec[Position]=deriveCodec
  lazy implicit val UserCodec:Codec[User]=deriveCodec
}
