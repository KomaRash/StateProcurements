import { Component, OnInit } from '@angular/core';
import {PurchaseService} from "../../services/purchase.service";

@Component({
  selector: 'app-purchase-system',
  templateUrl: './purchase-system.component.html',
  styleUrls: ['./purchase-system.component.css']
})
export class PurchaseSystemComponent implements OnInit {

  constructor(public purchaseListService:PurchaseService) { }


  ngOnInit(): void {
    this.purchaseListService.getPurchaseList()
  }

}
