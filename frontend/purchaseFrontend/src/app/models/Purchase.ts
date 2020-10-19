import {PurchaseLot} from "./PurchaseLot";

export interface Purchase{
    purchaseInfo: PurchaseInfo,
    description:String,
    readonly purchaseStatus:String
    purchaseLots:PurchaseLot[],
    purchaseId?:number
}
export interface PurchaseInfo{
    dateOfPurchase:Date,
    positionId?:number,
    procedureName:String
}
