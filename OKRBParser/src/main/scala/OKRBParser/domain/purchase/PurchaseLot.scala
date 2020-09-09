package OKRBParser.domain.purchase

import java.util.Date

import OKRBParser.domain.parseExcel.okrb.OKRBProduct

case class PurchaseLot(lotId:Option[Int],
                       okrb:OKRBProduct,
                       deadline:Date,
                       amount:Float,
                       name:String)

/**
create table if not exists PurchaseLot(
    PurchaseLotId SERIAL primary key ,
    PurchaseId SERIAL,
    ProductId SERIAL,
    Deadline date,
    LotAmount float,
    LotName varchar(255) not null,
    constraint OKRB_fk foreign key(ProductId) references okrb(productid),
    constraint Purchase_fk foreign key(PurchaseId) references Purchase(PurchaseId)
);
*/