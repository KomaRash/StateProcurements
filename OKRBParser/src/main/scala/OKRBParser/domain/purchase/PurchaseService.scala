package OKRBParser.domain.purchase

import OKRBParser.domain.position.{PositionId, UserId}
import OKRBParser.domain.purchase.purchaseLot.PurchaseLot
import cats.Monad
import cats.data.EitherT
import cats.implicits._

class PurchaseService[F[_]](repository: PurchaseRepositoryAlgebra[F],
                            validation: PurchaseValidationAlgebra[F]) {
  def updateLotAdmin(id: Option[PurchaseId], lot: PurchaseLot)(implicit M: Monad[F]): EitherT[F, PurchaseError, PurchaseLot] = {
    for {
      _ <- validation.exist(id)
      _ <- validation.lotExist(id, lot)
      saved <- EitherT.liftF(repository.updateLotInfo(id, lot))
    } yield saved
  }

  def updateLotUser(id: Option[PurchaseId], lot: PurchaseLot)(implicit M: Monad[F]): EitherT[F, PurchaseError, PurchaseLot] = {
    for {
      _ <- validation.alreadyExecution(id)
      _ <- validation.exist(id)
      _ <- validation.lotExist(id, lot)
      saved <- EitherT.liftF(repository.updateLotInfo(id, lot))
    } yield saved
  }

  def createPurchase(purchase: Purchase, positionId: Option[PositionId])(implicit M: Monad[F]): EitherT[F, PurchaseAlreadyExists, Option[Purchase]] = {
    for {
      _ <- validation.doesNotExist(purchase.description, purchase.purchaseInfo)
      saved <- EitherT.liftF(repository.createPurchase(purchase, positionId))
    } yield saved

  }

  def addLots(purchaseId: Option[PurchaseId], lots: List[PurchaseLot])(implicit M: Monad[F]): EitherT[F, PurchaseError, Option[Purchase]] = {
    for {
      _ <- validation.exist(purchaseId)
      _ <- validation.doesNotCreated(purchaseId)
      id <- EitherT.fromOptionF(purchaseId.pure[F], PurchaseNotFound)
      saved <- EitherT.liftF(repository.addLots(id, lots))
    } yield saved
  }

  def confirmCreatePurchase(purchaseId: Option[PurchaseId])(implicit M: Monad[F]): EitherT[F, PurchaseError, Option[Purchase]] = {
    for {
      _ <- validation.exist(purchaseId)
      id <- EitherT.fromOptionF(purchaseId.pure[F], PurchaseNotFound)
      purchase <- EitherT.liftF(repository.setStatus(PurchaseStatus.Execution, id))
    } yield purchase
  }

  def confirmCompletePurchase(purchaseId: PurchaseId)(implicit M: Monad[F]): EitherT[F, PurchaseError, Option[Purchase]] = for {
    purchase <- EitherT.liftF(repository.setStatus(PurchaseStatus.CompletedPurchase, purchaseId))
  } yield purchase

  def getPurchaseList(userId: UserId)(implicit M: Monad[F]): F[List[Purchase]] = {
    repository.getPurchaseList(userId)

  }

  def getPurchase(purchaseId: PurchaseId)(implicit M: Monad[F]): EitherT[F, PurchaseError, Option[Purchase]] = {
    for {
      _ <- validation.exist(purchaseId.some)
      purchase <- EitherT.liftF(repository.getPurchaseWithLots(purchaseId))
    } yield purchase
  }
}

object PurchaseService {
  def service[F[_] : Monad](repository: PurchaseRepositoryAlgebra[F]): PurchaseService[F] = {
    new PurchaseService[F](repository, new PurchaseValidationInterpreter[F](repository))
  }
}
