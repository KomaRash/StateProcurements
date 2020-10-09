package OKRBParser.domain.purchase

import OKRBParser.domain.{PurchaseAlreadyExists, PurchaseError, PurchaseNotFound}
import cats.Monad
import cats.data.EitherT
class PurchaseService[F[_]](repository: PurchaseRepositoryAlgebra[F],
                            validation:PurchaseValidationAlgebra[F]){
  def updateLot(id: Option[PurchaseId], lot: PurchaseLot)(implicit M: Monad[F]): EitherT[F, PurchaseError, PurchaseLot] = {
    for{
      _<-validation.exist(id)
      _<-validation.lotExist(id,lot)
      saved<-EitherT.liftF(repository.updateLotInfo(id,lot))
    }yield saved
  }


  def createPurchase(purchase: Purchase)(implicit M: Monad[F]):EitherT[F,PurchaseAlreadyExists,Option[Purchase]]= {
  for{
   _ <- validation.doesNotExist(purchase.description,purchase.purchaseInfo)
    saved <- EitherT.liftF(repository.createPurchase(purchase))
  } yield saved

  }
  def addLots(purchaseId: Option[PurchaseId],lots:List[PurchaseLot])(implicit M: Monad[F]): EitherT[F, PurchaseError, Option[Purchase]] =
    for{
      _<-validation.exist(purchaseId)
      _<-validation.doesNotCreated(purchaseId)
      id<-EitherT.fromOption(purchaseId,PurchaseNotFound)
      saved <-EitherT.liftF(repository.addLots(id,lots))
    } yield saved
  def confirmCreatePurchase(purchase: Purchase)(implicit M: Monad[F]):EitherT[F, PurchaseError, Option[Purchase]]={
      for{
      _<-validation.compare(purchase)
      id<-EitherT.fromOption(purchase.purchaseId,PurchaseNotFound)
      purchase<-EitherT.liftF(repository.setStatus(PurchaseStatus.Execution,id))
    }yield purchase
  }

  def getPurchaseList:F[List[Purchase]]={
    repository.getPurchaseList
  }

}
