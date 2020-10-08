package OKRBParser.domain.purchase

import OKRBParser.domain.{PurchaseAlreadyExists, PurchaseError, PurchaseNotFound}
import cats.Monad
import cats.data.EitherT
class PurchaseService[F[_]](repository: PurchaseRepositoryAlgebra[F],
                            validation:PurchaseValidationAlgebra[F]){



  def createPurchase(purchase: Purchase)(implicit M: Monad[F]):EitherT[F,PurchaseAlreadyExists,Purchase]= {
  for{
   _ <- validation.doesNotExist(purchase.description,purchase.purchaseInfo)
    saved <- EitherT.liftF(repository.createPurchase(purchase))
  } yield saved
  }
  def addLots(purchaseId: Option[PurchaseId],lots:List[PurchaseLot])(implicit M: Monad[F]): EitherT[F, PurchaseError, Purchase] =
    for{
      _<-validation.exist(purchaseId)
      _<-validation.doesNotCreated(purchaseId)
      id<-EitherT.fromOption(purchaseId,PurchaseNotFound)
      saved <-EitherT.liftF(repository.addLots(id,lots))
    } yield saved
  def confirmCreatePurchase(purchase: Purchase): Unit ={
/*    for{
      _<-validation.compare(purchase)
      saved <-EitherT.liftF(purchase)
    }yield */
  }
}
