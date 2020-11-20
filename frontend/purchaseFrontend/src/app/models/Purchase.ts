import {PurchaseLot} from "./PurchaseLot";

export class Purchase{
    purchaseInfo: PurchaseInfo;
    description:String;
    readonly purchaseStatus:String;
    purchaseLots:PurchaseLot[];
    purchaseId?:number
    constructor() {
        this.purchaseLots=[];
        this.purchaseStatus="CreatedPurchase";
        this.description="";
        this.purchaseInfo={
            procedureName:"",
            dateOfPurchase:new Date()
        }
    }
    toJson():string{
      return JSON.stringify(this)
    }
}
export class PurchaseInfo{
    dateOfPurchase:Date;
    positionId?:number;
    procedureName:String
}
