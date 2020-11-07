import { Component, OnInit } from '@angular/core';
import {PurchaseLot} from "../../models/PurchaseLot";
import {PurchaseService} from "../../services/purchase.service";
import {CreateLotComponent} from "./create-lot/create-lot.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-create-purchase',
  templateUrl: './create-purchase.component.html',
  styleUrls: ['./create-purchase.component.css']
})
export class CreatePurchaseComponent implements OnInit {
  purchaseLots: PurchaseLot[]=[];
  enableEdit: boolean=false;

  constructor(public  purchaseService: PurchaseService,public dialog: MatDialog) {

  }

  ngOnInit(): void {
  }

  createLot() {
    let dialogRef = this.dialog.open(CreateLotComponent, {
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`); // Pizza!
    });
  }
}
