package OKRBParser.domain.purchase

import cats.data.EitherT
trait PurchaseValidationAlgebra[F[_]] {
  def doesNotExist(description:String,purchaseInfo: PurchaseInfo):EitherT[F,PurchaseAlreadyExists,Unit]
  def exist(purchaseId:Option[PurchaseId]):EitherT[F,PurchaseError,Unit]
  def lotExist(purchaseId: Option[PurchaseId],lot: PurchaseLot):EitherT[F,PurchaseError,Unit]
  def doesNotCreated(purchaseId: Option[PurchaseId]):EitherT[F,PurchaseError,Unit]
  def compare(purchase: Purchase):EitherT[F,PurchaseError,Unit]
  def alreadyExecution(purchaseId: Option[PurchaseId]):EitherT[F,PurchaseError,Unit]
}
