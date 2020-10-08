package OKRBParser.infrastructure.endpoints
import OKRBParser.domain.{NotCorrectDataPurchase, PurchaseAlreadyExecution, PurchaseAlreadyExists, PurchaseNotFound}
import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User}
import OKRBParser.domain.purchase._
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import org.joda.time.DateTime

import scala.util.Try

class PurchaseEndpoints[F[_]:ConcurrentEffect:Monad](service:PurchaseService[F]) extends Http4sDsl[F]{



  implicit val purchaseDecoder: EntityDecoder[F, Purchase] = jsonOf[F,Purchase]
  implicit val infoDecoder = jsonOf[F,PurchaseInfo]
  implicit val positionDecoder = jsonOf[F,Position]
  implicit val userDecoder = jsonOf[F,User]
  implicit val okrbProductDecoder = jsonOf[F,OKRBProduct]
  implicit val PurchaseLotsDecoder = jsonOf[F,PurchaseLot]
  implicit val DateTimeDecoder = jsonOf[F,DateTime]
  implicit val lotsEncoder = jsonOf[F,List[PurchaseLot]]




  def endpoint:HttpRoutes[F]=HttpRoutes.of[F] {

    case req@POST -> Root /"test" =>{
      Ok(req.as[Purchase])
    }
    case GET->Root/"purchases"=>{
      Ok(service.getPurchaseList)
    }
    /**
     *  Добавление лотов для новой закупки
     */
    case req@POST -> Root /"purchase"/id/"lots"=>{
     val purchase=for {
     lots <-req.as[List[PurchaseLot]]
      p<-service.addLots(Try(id.toInt).toOption,lots).value
     }yield p
      purchase.flatMap{
        case Right(value) =>Ok(value)
        case Left(PurchaseAlreadyExecution)=>Ok("заявка уже выполняется")
        case Left(PurchaseNotFound)=>Ok("заявка не найдена")

      }
    }

    /**
     *  Добавление новой закупки
     */
    case req@POST -> Root / "purchase"=> {
      val purchases = for {
        purchase <- req.as[Purchase]
        p <- service.createPurchase(purchase).value
      } yield p
      purchases.flatMap {
        case Left(PurchaseAlreadyExists(purchase)) => Ok(s"создано ${purchase.purchaseId}")
        case Right(value) => Ok(value)
      }
    }

    /**
     * подтверждение и сохраниние закупки
     */
    case req@GET->Root/ "purchase"=>{
    val purchase=for{
      purchase<-req.as[Purchase]
      p<-service.confirmCreatePurchase(purchase).value
    }yield p
      purchase.flatMap {
        case Left(PurchaseNotFound) | Right(None)=>Ok("Заявка не найдена")
        case Right(Some(value)) =>Ok(value)
        case Left(NotCorrectDataPurchase)=>Ok("Не валидные данные")
      }
    }
  }

}
