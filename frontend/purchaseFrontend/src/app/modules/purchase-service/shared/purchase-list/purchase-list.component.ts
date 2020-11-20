import {Component, OnInit} from '@angular/core';
import {Purchase} from "../../../../models/Purchase";
import {PurchaseService} from "../../../../services/purchase.service";

@Component({
  selector: 'app-purchase-list',
  templateUrl: './purchase-list.component.html',
  styleUrls: ['./purchase-list.component.css']
})
export class PurchaseListComponent implements OnInit {
  panelOpenState = false;
  purchases: Array<Purchase>

  constructor(public purchaseListService: PurchaseService) {

  }

  ngOnInit(): void {
    this.purchaseListService.getPurchaseList().subscribe(p =>
      this.purchases = p)
  }


}
