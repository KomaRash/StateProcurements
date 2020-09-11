package OKRBParser.domain.purchase

import OKRBParser.domain._
import cats.data.EitherT
trait PurchaseValidationAlgebra[F[_]] {
def doesNotExist(description:String,purchaseInfo: PurchaseInfo):EitherT[F,PurchaseAlreadyExists,Unit]
def exist(purchaseId:Option[PurchaseId]):EitherT[F,PurchaseNotFound.type,Unit]
}
