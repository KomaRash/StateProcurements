import {OKRBProduct} from "./OKRBProduct";

export interface PurchaseLot {
    okrb: OKRBProduct,
    deadline: Date,
    amount: number,
    name: String,
    purchaseLotStatus:String,
    lotId?: number
}
