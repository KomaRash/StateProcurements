import {Component, Input, OnInit} from '@angular/core';
import {PurchaseLot} from "../../../../models/PurchaseLot";

@Component({
  selector: 'app-purchase-lots-table',
  templateUrl: './purchase-lots-table.component.html',
  styleUrls: ['./purchase-lots-table.component.css']
})
export class PurchaseLotsTableComponent implements OnInit {
  @Input() purchaseLots: PurchaseLot[];
  @Input() updateTable:boolean
  constructor() { }

  ngOnInit(): void {
  }

}
