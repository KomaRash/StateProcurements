package OKRBParser.infrastructure.endpoints
import OKRBParser.domain.auth.{Auth, AuthService}
import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import OKRBParser.domain.position.{Position, User}
import OKRBParser.domain.purchase._
import cats.Monad
import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, Header, Headers, HttpRoutes}
import org.joda.time.DateTime
import tsec.authentication._

import scala.util.Try

class PurchaseEndpoints[F[_]:ConcurrentEffect:Monad](service:PurchaseService[F],authService:AuthService[F]) extends Http4sDsl[F]{
  implicit val purchaseDecoder: EntityDecoder[F, Purchase] = jsonOf[F,Purchase]
  implicit val infoDecoder = jsonOf[F,PurchaseInfo]
  implicit val positionDecoder = jsonOf[F,Position]
  implicit val userDecoder = jsonOf[F,User]
  implicit val okrbProductDecoder = jsonOf[F,OKRBProduct]
  implicit val PurchaseLotsDecoder = jsonOf[F,PurchaseLot]
  implicit val DateTimeDecoder = jsonOf[F,DateTime]
  implicit val lotsEncoder = jsonOf[F,List[PurchaseLot]]

  private def getPurchase:AuthEndpoint[F, Token]={
    case GET-> Root/"purchases" asAuthed user=>{
      val purchaseList=user.userID.map(service.getPurchaseList(_))
      purchaseList match {
        case Some(value) => Ok(value)
        case None =>NotFound()
      }
    }
  }
  private def createPurchase:AuthEndpoint[F,Token]={
    case req@POST -> Root / "purchase" asAuthed user=>
      (for {
        purchase <- req.request.as[Purchase]
        p <- service.createPurchase(purchase,user.position.positionId).value
      } yield p).flatMap {
        case Left(PurchaseAlreadyExists(purchase)) => Conflict()
        case Right(value) => Ok(value)
      }
    case req@POST -> Root /"purchase"/id/"lots" asAuthed user=>
      (for {
        lots <-req.request.as[List[PurchaseLot]]
        p<-service.addLots(Try(id.toInt).toOption,lots,user).value
      }yield p).flatMap{
        case Right(value) =>Ok(value)
        case Left(PurchaseAlreadyExecution)=>Conflict()
        case Left(PurchaseNotFound)=>NotFound()
      }
    case req@PUT ->Root/"purchase"/id/"lots" asAuthed user=>
      (for{
        lot<-req.request.as[PurchaseLot]
        p<-service.updateLotUser(Try(id.toInt).toOption,lot,user).value
      }yield p).flatMap {
        case Left(PurchaseNotFound) => NotFound()
        case Left(PurchaseLotNotFound) => NotFound()
        case Right(value) =>Ok(value)
        case Left(PurchaseLotNotFound)=>NotFound()
        case Left(NotCorrectDataPurchase)=>UnprocessableEntity()
      }
    case req@GET->Root/ "purchase" asAuthed user=>
      val purchase=for{
        purchase<-req.request.as[Purchase]
        p<-service.confirmCreatePurchase(purchase,user).value
      }yield p
      purchase.flatMap {
        case Left(PurchaseNotFound) |Right(None) =>NotFound()
        case Right(Some(value)) =>Ok(value)
        case Left(NotCorrectDataPurchase)=>UnprocessableEntity()
      }

  }
  private def updatePurchase:AuthEndpoint[F,Token]={
    case req@PUT ->Root/"purchase"/id/"lots" asAuthed user=>
      (for{
        lot<-req.request.as[PurchaseLot]
        p<-service.updateLotAdmin(Try(id.toInt).toOption,lot,user).value
      }yield p).flatMap {
        case Left(PurchaseNotFound) =>NotFound()
        case Left(PurchaseLotNotFound) => NotFound()
        case Right(value) =>Ok(value)
        case Left(PurchaseLotNotFound)=>NotFound()
        case Left(PurchaseAlreadyExecution)=>Conflict()
        case Left(NotCorrectDataPurchase)=>UnprocessableEntity()
      }
  }
  /*private def deletePurchase:AuthEndpoint[F,Token]={
    case DELETE ->Root/"purchase"/id/"lots" asAuthed user=>
      service.deletePurchase
  }*/
  def endpoints:  HttpRoutes[F]={
    val authEndpoints: TSecAuthService[User, Token, F] ={
      val adminEndpoints=updatePurchase
      (Auth.directorOnly(adminEndpoints))
    }
    val userEndpoints={
      val userEndpoints=getPurchase.orElse(createPurchase)
      Auth.userOnly(userEndpoints)
    }
    authService.auth.liftService(userEndpoints)<+>
    authService.auth.liftService(authEndpoints)
  }
}
object PurchaseEndpoints{
  def endpoints[F[_]:ConcurrentEffect:Monad](service:PurchaseService[F],
                                             auth:AuthService[F]): HttpRoutes[F] ={
    new PurchaseEndpoints[F](service,auth).endpoints.map(_.withHeaders(Headers.of(
      Header("Access-Control-Allow-Origin", "http://localhost:4200"),
      Header("Access-Control-Allow-Credentials","true"))))
  }
}