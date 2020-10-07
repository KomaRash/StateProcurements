package OKRBParser.domain.purchase


import org.joda.time.DateTime
case class Purchase(purchaseInfo: PurchaseInfo,
                    description:String,
                    purchaseStatus:PurchaseStatus,
                    purchaseLots:List[PurchaseLot]=List(),
                    purchaseId:Option[PurchaseId]=None)

case class PurchaseInfo( dateOfPurchase:DateTime,
                         positionId: PositionId,
                         procedureName:String)

/**
create table if not exists Purchase
(   PurchaseId     SERIAL primary key ,
    DateOfPurchase date,
    PositionId SERIAL,
    ProcedureName     varchar(255) Not Null,
    constraint Position_fk foreign key (PositionId) references MilitaryPosition(PositionId)
);
*/
