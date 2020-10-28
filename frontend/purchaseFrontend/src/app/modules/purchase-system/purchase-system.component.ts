import { Component, OnInit } from '@angular/core';
import {PurchaseListService} from "../../services/purchase.list.service";

@Component({
  selector: 'app-purchase-system',
  templateUrl: './purchase-system.component.html',
  styleUrls: ['./purchase-system.component.css']
})
export class PurchaseSystemComponent implements OnInit {

  constructor(public purchaseListService:PurchaseListService) { }


  ngOnInit(): void {
    this.purchaseListService.getPurchaseList()
  }

}
