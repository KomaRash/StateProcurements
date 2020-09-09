package OKRBParser.domain.purchase

import java.util.Date

import OKRBParser.domain.position.Position
import cats.data.NonEmptyList

case class Purchase(purchaseId:Option[Int],
                    purchaseInfo: PurchaseInfo,
                    purchaseLots:NonEmptyList[PurchaseLot])
case class PurchaseInfo(dateOfPurchase:Date,
                        position: Position,
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
