import {Component, OnInit} from '@angular/core';
import {PurchaseLot} from "../../models/PurchaseLot";
import {PurchaseService} from "../../services/purchase.service";
import {CreateLotComponent} from "./create-lot/create-lot.component";
import {MatDialog} from "@angular/material/dialog";
import {Purchase} from "../../models/Purchase";

@Component({
  selector: 'app-create-purchase',
  templateUrl: './create-purchase.component.html',
  styleUrls: ['./create-purchase.component.css']
})
export class CreatePurchaseComponent implements OnInit {
  purchaseLots: PurchaseLot[] = [];
  enableEdit: boolean = false;
  purchase: Purchase = {
    purchaseInfo: {
      dateOfPurchase:new Date(),
      positionId: undefined,
      procedureName: "",
    },
    description: "",
    purchaseStatus: "CreatedPurchase",
    purchaseLots: [],
    purchaseId:null
  };

  constructor(public  purchaseService: PurchaseService, public dialog: MatDialog) {

  }

  ngOnInit(): void {
  }

  createPurchaseLot() {
    let dialogRef = this.dialog.open(CreateLotComponent, {
      data: {}
    });
    dialogRef.afterClosed().subscribe((result: PurchaseLot) => {
      //  console.log(result)
      if (result != undefined) {
        this.purchaseLots.push(result)
      }
    }, error => {
    });
  }

  savePurchase() {
    this.purchaseService.savePurchase(this.purchase).subscribe(purchase=>{
      this.purchase=purchase
      console.log(this.purchase)
    })
  }
  savePurchaseLot() {
    
  }
}
