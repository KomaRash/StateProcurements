package OKRBParser.infrastructure.endpoints
import java.util.Date

import OKRBParser.domain.position.Position
import OKRBParser.domain.purchase.{Purchase, PurchaseInfo, PurchaseLot, PurchaseStatus}
import cats.Monad
import cats.effect.ConcurrentEffect
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
class PurchaseEndpoints[F[_]:ConcurrentEffect:Monad]() extends Http4sDsl[F]{
  def endpoint:HttpRoutes[F]=HttpRoutes.of[F] {
    case req@POST -> Root / "TestPurchases" => {
      val a = req.as[PurchaseLot]
      val purchaseInfo:PurchaseInfo=PurchaseInfo(new Date(18,6,1999),Position("Ваня","Майор"),"Аукцион")
      val purchase: Purchase=Purchase(purchaseInfo,"template",PurchaseStatus.Execution,List())
      Ok(purchase)
    }
  }

}
