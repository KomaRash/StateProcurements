import {Component, Input, OnInit, Output} from '@angular/core';
import {PurchaseLot} from "../../../../models/PurchaseLot";
import {OKRBProduct} from "../../../../models/OKRBProduct";
import {EventEmitter} from "events";
import {MatDialog} from "@angular/material/dialog";
import {PurchaseLotDetailComponent} from "../purchase-lot-detail/purchase-lot-detail.component";

@Component({
  selector: 'app-purchase-lots-table',
  templateUrl: './purchase-lots-table.component.html',
  styleUrls: ['./purchase-lots-table.component.css']
})
export class PurchaseLotsTableComponent implements OnInit {
  @Input() purchaseLots: PurchaseLot[];
  @Input() update: boolean
  @Input() add: boolean
  @Output() purchaseLotChanged = new EventEmitter<Array<PurchaseLot>>()

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  getOKRB(okrb: OKRBProduct) {
    return okrb.section + '.' +
      okrb.productClass + '.' +
      okrb.subCategories + '.' +
      okrb.grouping
  }

  createLot() {
    let dialogRef = this.dialog.open(PurchaseLotDetailComponent, {
      data: new PurchaseLot()
    });
    dialogRef.afterClosed().subscribe((result: PurchaseLot) => {
      //  console.log(result)
      if (result != undefined) {
        this.purchaseLots.push(result)
      }
    }, error => {
    });
  }
  editLot(i:number){
    let dialogRef = this.dialog.open(PurchaseLotDetailComponent, {
      data: this.purchaseLots[i]
    });
    dialogRef.afterClosed().subscribe((result: PurchaseLot) => {
      //  console.log(result)
      if (result != undefined) {
        this.purchaseLots[i]=result;
      }
    }, error => {
    });

  }

}

