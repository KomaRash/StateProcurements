import { Component, OnInit } from '@angular/core';
import {PurchaseLot} from "../../models/PurchaseLot";
import {PurchaseService} from "../../services/purchase.service";

@Component({
  selector: 'app-create-purchase',
  templateUrl: './create-purchase.component.html',
  styleUrls: ['./create-purchase.component.css']
})
export class CreatePurchaseComponent implements OnInit {
  purchaseLots: PurchaseLot[]=[];
  enableEdit: boolean=false;

  constructor(public  purchaseService: PurchaseService) {

  }

  ngOnInit(): void {
  }

}
