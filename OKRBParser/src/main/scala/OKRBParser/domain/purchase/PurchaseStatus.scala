package OKRBParser.domain.purchase
import enumeratum._
trait PurchaseStatus
case object PurchaseStatus extends Enum[PurchaseStatus] with CirceEnum[PurchaseStatus] {
  case object CreatedPurchase extends PurchaseStatus
  case object Execution extends PurchaseStatus
  case object CompletedPurchase extends PurchaseStatus

  val values = findValues
}
