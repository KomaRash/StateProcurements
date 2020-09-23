package OKRBParser.infrastructure.endpoints
import java.util.Date

import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User}
import OKRBParser.domain.purchase.{Purchase, PurchaseInfo, PurchaseLot, PurchaseService, PurchaseStatus}
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

class PurchaseEndpoints[F[_]:ConcurrentEffect:Monad](service:PurchaseService[F]) extends Http4sDsl[F]{



  implicit val purchaseDecoder: EntityDecoder[F, Purchase] = jsonOf[F,Purchase]
  implicit val infoDecoder = jsonOf[F,PurchaseInfo]
  implicit val positionDecoder = jsonOf[F,Position]
  implicit val userDecoder = jsonOf[F,User]
  implicit val okrbProductDecoder = jsonOf[F,OKRBProduct]
  implicit val PurchaseLotsDecoder = jsonOf[F,PurchaseLot]
  implicit val DateTimeDecoder = jsonOf[F,Date]



  def endpoint:HttpRoutes[F]=HttpRoutes.of[F] {
    case req@POST -> Root / "TestPurchases" => {
      val purchaseInfo:PurchaseInfo=PurchaseInfo(/*new Date(18,6,1999),*/1,"Аукцион")
      val purchase: Purchase=Purchase(purchaseInfo,"template",PurchaseStatus.Execution,List())
      Ok(purchase)
    }
    case req@POST -> Root /"test" =>{
      Ok(req.as[Purchase])
    }
    case req@POST -> Root / "AddPurchases"=>{
      val a=for{
        purchase<-req.as[Purchase]
        p<- service.createPurchase(purchase).value
      }yield p
      a.flatMap {
        case Left(value) => Ok("создано")
        case Right(value) =>Ok(value)
      }
    }
  }

}
