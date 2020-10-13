package OKRBParser.domain.purchase
import OKRBParser.domain.purchase.PurchaseStatus.CreatedPurchase
import OKRBParser.domain._
import cats.Monad
import cats.data.EitherT
import cats.implicits._

class PurchaseValidationInterpreter[F[_]:Monad](repository: PurchaseRepositoryAlgebra[F])
  extends PurchaseValidationAlgebra[F] {
  override def doesNotExist(description: String, purchaseInfo: PurchaseInfo): EitherT[F, PurchaseAlreadyExists, Unit] = {
    repository.
      findByInfoAndDescription(description,purchaseInfo).
      toLeft(()).
      leftMap(PurchaseAlreadyExists)
    }

  override def exist(purchaseId: Option[PurchaseId]): EitherT[F, PurchaseNotFound.type, Unit] = {
    purchaseId match {
      case Some(id) => EitherT.fromOptionF(repository.getPurchase(id),PurchaseNotFound).as(())
      case None =>EitherT.fromEither(Left(PurchaseNotFound))
    }
  }

  override def doesNotCreated(purchaseId: Option[PurchaseId]): EitherT[F, PurchaseError, Unit] = {
     purchaseId match {
      case Some(id) =>
      EitherT{repository.getStatus(id).map {
        case Some(CreatedPurchase) =>().asRight
        case None=>PurchaseNotFound.asLeft
        case _ => PurchaseAlreadyExecution.asLeft
        }
      }
      case None => EitherT.fromEither(PurchaseNotFound.asLeft)
    }
    }

  override def compare(purchase: Purchase): EitherT[F, PurchaseError, Unit] = {
    purchase.purchaseId match {
      case Some(purchaseId) =>EitherT.fromOptionF(repository.
        getPurchaseWithLots(purchaseId),PurchaseNotFound).
        ensure(NotCorrectDataPurchase){p=>purchase.equals(p)}.as(())
      case None =>EitherT.fromEither(Left(PurchaseNotFound))
    }

  }

  override def lotExist(purchaseId: Option[PurchaseId], lot: PurchaseLot): EitherT[F, PurchaseError, Unit] = {
    purchaseId  match {
      case Some(id) => EitherT.
        fromOptionF(repository.getPurchaseLots(id).map(_.find(_.lotId==lot.lotId)),PurchaseLotNotFound).
        as().asInstanceOf[ EitherT[F, PurchaseError, Unit]]
      case None =>EitherT.fromEither(PurchaseNotFound.asLeft)
    }
  }
}
