package OKRBParser.domain.purchase


import OKRBParser.domain.position.PositionId
import OKRBParser.domain.purchase.purchaseLot.PurchaseLot
import org.joda.time.DateTime

case class Purchase(purchaseInfo: PurchaseInfo,
                    description: String,
                    purchaseStatus: PurchaseStatus,
                    purchaseLots: List[PurchaseLot] = List(),
                    purchaseId: Option[PurchaseId] = None)

case class PurchaseInfo(dateOfPurchase: DateTime,
                        positionId: PositionId,
                        procedureName: String)

/**
 * create table if not exists Purchase
 * (   PurchaseId     SERIAL primary key ,
 * DateOfPurchase date,
 * PositionId SERIAL,
 * ProcedureName     varchar(255) Not Null,
 * constraint Position_fk foreign key (PositionId) references MilitaryPosition(PositionId)
 * );
 */
/*
{
        "purchaseInfo": {
            "dateOfPurchase": "28/12/2020",
            "positionId": 1,
            "procedureName": "Гос закупка"
        },
        "description": "штаны",
        "purchaseStatus": "Execution",
        "purchaseLots": [],
        "purchaseId": 4
    }
 */
