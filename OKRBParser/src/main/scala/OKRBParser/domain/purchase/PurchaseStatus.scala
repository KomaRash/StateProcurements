package OKRBParser.domain.purchase

import enumeratum._

sealed trait PurchaseStatus extends EnumEntry

case object PurchaseStatus extends Enum[PurchaseStatus] with CirceEnum[PurchaseStatus] {


  case object CreatedPurchase extends PurchaseStatus

  case object Execution extends PurchaseStatus

  case object CompletedPurchase extends PurchaseStatus

  val values = findValues
}
