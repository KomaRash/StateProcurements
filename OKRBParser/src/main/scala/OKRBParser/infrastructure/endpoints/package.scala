package OKRBParser.infrastructure

import java.util.Date

import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User}
import OKRBParser.domain.purchase.{Purchase, PurchaseInfo, PurchaseLot}
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._

import scala.util.control.NonFatal
package object endpoints {
/*
  lazy implicit val dEncoder: Encoder[D] = deriveEncoder[D]
  lazy implicit val dDecoder: Decoder[D] = deriveDecoder[D]
*/


  lazy implicit val OKRBProductEncoder:Encoder[OKRBProduct]=deriveEncoder[OKRBProduct]
  lazy implicit val OKRBProductDecoder:Decoder[OKRBProduct]=deriveDecoder[OKRBProduct]
  lazy implicit val DateEncoder:Encoder[Date]=(a:Date)=>a.toString.asJson
  implicit val decodeDateTime: Decoder[Date] = Decoder.decodeString.emap { s =>
    try {
      Right(new Date(s))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }
  lazy implicit val PurchaseLotCodec: Codec[PurchaseLot] = deriveCodec
  lazy implicit val PurchaseCodec:Codec[Purchase]=deriveCodec
  lazy implicit val PurchaseInfoCodec:Codec[PurchaseInfo]=deriveCodec
  lazy implicit val PositionCodec:Codec[Position]=deriveCodec
  lazy implicit val UserCodec:Codec[User]=deriveCodec
}
