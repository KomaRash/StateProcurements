package OKRBParser.domain.purchase

import OKRBParser.domain.parseExcel.okrb.OKRBProduct
import org.joda.time.DateTime

case class PurchaseLot(okrb: OKRBProduct,
                       deadline: DateTime,
                       amount: Float,
                       name: String,
                       lotId: Option[Int] = None)

/**
 * create table if not exists PurchaseLot(
 * PurchaseLotId SERIAL primary key ,
 * PurchaseId SERIAL,
 * ProductId SERIAL,
 * Deadline date,
 * LotAmount float,
 * LotName varchar(255) not null,
 * constraint OKRB_fk foreign key(ProductId) references okrb(productid),
 * constraint Purchase_fk foreign key(PurchaseId) references Purchase(PurchaseId)
 * );
 */