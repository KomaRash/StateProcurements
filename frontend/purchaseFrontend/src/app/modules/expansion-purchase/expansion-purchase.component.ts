import { Component, OnInit } from '@angular/core';
import {Purchase} from "../../models/Purchase";
import {PurchaseService} from "../../services/purchase.service";

@Component({
  selector: 'app-expansion-purchase',
  templateUrl: './expansion-purchase.component.html',
  styleUrls: ['./expansion-purchase.component.css']
})
export class ExpansionPurchaseComponent implements OnInit {
  panelOpenState = false;
  purchases:Array<Purchase>
  constructor(public purchaseListService:PurchaseService){

  }

  ngOnInit(): void {
    this.purchaseListService.getPurchaseList().subscribe(p=>
      this.purchases=p)
  }

}
