package OKRBParser.domain.purchase.purchaseLot

import OKRBParser.domain.okrb.OKRBProduct
import org.joda.time.DateTime

case class PurchaseLot(okrb: OKRBProduct,
                       deadline: DateTime,
                       amount: Float,
                       name: String,
                       lotStatus: PurchaseLotStatus,
                       lotId: Option[Int] = None)
