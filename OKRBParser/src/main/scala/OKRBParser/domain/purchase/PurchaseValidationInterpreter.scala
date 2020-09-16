package OKRBParser.domain.purchase
import OKRBParser.domain.{PurchaseAlreadyExists, PurchaseNotFound}
import cats.Monad
import cats.data.EitherT

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
      case Some(id) => EitherT.fromOptionF(repository.getById(id),PurchaseNotFound).map(_ =>())
      case None =>EitherT.fromEither(Left(PurchaseNotFound))
    }
  }


}
