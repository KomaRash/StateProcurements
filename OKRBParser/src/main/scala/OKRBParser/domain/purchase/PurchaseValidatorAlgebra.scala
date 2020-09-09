package OKRBParser.domain.purchase

trait PurchaseValidatorAlgebra[F[_]] {
def isValidOKRBProduct(purchaseInfo: PurchaseInfo):
}
