import {OKRBProduct} from "./OKRBProduct";

export class PurchaseLot {
  okrb: OKRBProduct;
  deadline: Date;
  amount: number;
  name: String;
  lotStatus: String;
  lotId?: number
  constructor() {
    this.lotStatus="CreatedLot"
    this.lotId=-1
  }
}
