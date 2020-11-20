import {Component, OnInit} from '@angular/core';
import {Purchase} from "../../../../models/Purchase";
import {PurchaseService} from "../../../../services/purchase.service";
import {Route, Router} from "@angular/router";

@Component({
  selector: 'app-create-purchase',
  templateUrl: './create-purchase.component.html',
  styleUrls: ['./create-purchase.component.css']
})
export class CreatePurchaseComponent implements OnInit {
  purchase: Purchase
  edit: boolean;
  saved: boolean;

  constructor(private purchaseService: PurchaseService,private router:Router) {
    this.edit = true
    this.saved = false
    this.purchase = new Purchase();

  }

  ngOnInit(): void {
  }

  save() {
    this.purchaseService.savePurchase(this.purchase).subscribe(purchase => {
      purchase.purchaseLots = this.purchase.purchaseLots
      this.purchaseService.savePurchaseLots(purchase).subscribe(purchase => {

        this.purchase.purchaseId = purchase.purchaseId
        this.purchase.purchaseLots = purchase.purchaseLots
      })
    })

    this.saved = true;
  }

  isCanSave(): boolean {
    return this.saved
  }

  confirm() {
    this.purchaseService.confirmPurchaseCreate(this.purchase).subscribe((p: Purchase) => {
    this.router.navigate(['user/purchases'])
    })
  }

  delete() {

  }
}
