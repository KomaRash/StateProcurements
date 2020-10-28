import { Component, OnInit } from '@angular/core';
import {Purchase} from "../../models/Purchase";
import {PurchaseListService} from "../../services/purchase.list.service";

@Component({
  selector: 'app-expansion-purchase',
  templateUrl: './expansion-purchase.component.html',
  styleUrls: ['./expansion-purchase.component.css']
})
export class ExpansionPurchaseComponent implements OnInit {
  panelOpenState = false;
  purchases:Array<Purchase>
  constructor(public purchaseListService:PurchaseListService){

  }

  ngOnInit(): void {
    this.purchaseListService.getPurchaseList().subscribe(p=>
      this.purchases=p)
  }

}
