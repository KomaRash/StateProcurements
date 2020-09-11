package OKRBParser.domain

import OKRBParser.domain.purchase.Purchase

sealed trait Errors extends Product with Serializable
sealed trait PurchaseError extends Errors
case class PurchaseAlreadyExists(purchase:Purchase) extends PurchaseError
case object PurchaseNotFound extends PurchaseError
