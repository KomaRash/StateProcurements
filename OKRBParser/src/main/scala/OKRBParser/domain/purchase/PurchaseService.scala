package OKRBParser.domain.purchase

class PurchaseService[F[_]](purchaseRepository: PurchaseRepositoryAlgebra[F],
                            purchaseValidator:PurchaseValidatorAlgebra[F]){

}
