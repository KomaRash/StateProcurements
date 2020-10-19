import {OKRBProduct} from "./OKRBProduct";

export interface PurchaseLot {
    okrb: OKRBProduct,
    deadline: Date,
    amount: number,
    name: String,
    lotId?: number
}
