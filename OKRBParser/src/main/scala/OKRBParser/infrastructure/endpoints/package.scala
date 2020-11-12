package OKRBParser.infrastructure

import OKRBParser.domain.document.DocumentInfo
import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User, UserId, UsernamePasswordCredentials}
import OKRBParser.domain.purchase.{Purchase, PurchaseInfo, PurchaseLot}
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import org.http4s.Response
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}
import tsec.authentication.{SecuredRequest, TSecBearerToken}

import scala.util.control.NonFatal

package object endpoints {
  type Token = TSecBearerToken[UserId]
  type AuthEndpoint[F[_], Auth] =
    PartialFunction[SecuredRequest[F, User, Auth], F[Response[F]]]
  implicit val UsernamePasswordCredentialsCodec: Codec[UsernamePasswordCredentials] = deriveCodec
//2012-04-23T18:25:43.511Z
  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
  lazy implicit val OKRBProductEncoder: Encoder[OKRBProduct] = deriveEncoder[OKRBProduct]
  lazy implicit val OKRBProductDecoder: Decoder[OKRBProduct] = deriveDecoder[OKRBProduct]
  lazy implicit val DateEncoder: Encoder[DateTime] = (a: DateTime) => a.toString(dateFormatter).asJson
  implicit val decodeDateTime: Decoder[DateTime] = Decoder.decodeString.emap { s =>
    try {
      Right(DateTime.parse(s, dateFormatter))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }
  private def prepareDecoder(json: Json)(fieldType:String)(cursor: ACursor):ACursor={
    val field=cursor.downField(fieldType)
    if(field.failed)
    {
      cursor.withFocus(_.mapObject(_.add(fieldType,json)))
    }
    else field.up
  }
  private val prepareDecoderString=prepareDecoder(Json.fromInt(-1)) _
  lazy implicit val PurchaseLotCodec: Codec[PurchaseLot] = deriveCodec
  lazy implicit val PurchaseCodec: Encoder[Purchase] = deriveEncoder
  lazy implicit val PurchaseDecoder: Decoder[Purchase] = deriveDecoder[Purchase].prepare(prepareDecoderString("purchaseId"))
  lazy implicit val PurchaseLotDecoder: Decoder[PurchaseInfo] = deriveDecoder[PurchaseInfo].prepare(prepareDecoderString("positionId"))
  lazy implicit val PurchaseInfoCodec: Encoder[PurchaseInfo] = deriveCodec
  lazy implicit val PositionCodec: Codec[Position] = deriveCodec
  lazy implicit val UserCodec: Codec[User] = deriveCodec
  lazy implicit val DocumentInfoCodec:Codec[DocumentInfo]=deriveCodec
}
