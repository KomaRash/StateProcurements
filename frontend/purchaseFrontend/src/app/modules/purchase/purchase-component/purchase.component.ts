import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {PurchaseLot} from "../../../models/PurchaseLot";
import {Purchase} from "../../../models/Purchase";
import {PurchaseService} from "../../../services/purchase.service";

@Component({
  selector: 'app-purchase-component',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css']
})
export class PurchaseComponent implements OnInit {
  purchaseId:string;
  purchase:Purchase;
  purchaseForm: FormGroup
  sub:Subscription;
  enableEdit: boolean;
  purchaseLots:PurchaseLot[]=[];
  constructor(public route: ActivatedRoute,private fb: FormBuilder,public purchaseService:PurchaseService){
    this.purchaseForm=this.fb.group({
      purchaseDescription:[''],
      procedureName:[''],
      date:[''],
      purchaseStatus:[''],
    })
    this.enableEdit=false;
    this.sub = this.route.params.subscribe(params => {
      this.purchaseId =  params['id'];
      console.log(this.purchaseId)
    });


  }

  ngOnInit(): void {
    this.purchaseService.getPurchaseInfo(this.purchaseId).subscribe(purchase => {
      this.purchase = purchase
      this.purchaseLots=purchase.purchaseLots
      console.log(purchase)
      this.purchaseForm = this.fb.group({
        purchaseDescription: [this.purchase.description],
        procedureName: [this.purchase.purchaseInfo.procedureName],
        date: [this.purchase.purchaseInfo.dateOfPurchase],
        purchaseStatus:[this.purchase.purchaseStatus],
      });
    });
    this.purchaseForm.disable();

  }

  saveSegment() {

  }
}
