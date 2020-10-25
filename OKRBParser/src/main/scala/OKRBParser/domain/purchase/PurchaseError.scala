package OKRBParser.domain.purchase

import OKRBParser.domain.Err

sealed trait PurchaseError extends Err
case class PurchaseAlreadyExists(purchase:Purchase) extends PurchaseError
case object PurchaseNotFound extends PurchaseError
case object PurchaseLotNotFound extends PurchaseError
case object NotCorrectDataPurchase extends PurchaseError
case object PurchaseNotCreated extends PurchaseError
case object PurchaseAlreadyExecution extends PurchaseError
