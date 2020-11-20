package OKRBParser.domain.purchase.purchaseLot

import enumeratum._

sealed trait PurchaseLotStatus extends EnumEntry

case object PurchaseLotStatus extends Enum[PurchaseLotStatus] with CirceEnum[PurchaseLotStatus] {


  case object CreatedLot extends PurchaseLotStatus

  case object Execution extends PurchaseLotStatus

  case object CompletedLot extends PurchaseLotStatus

  val values = findValues
}


