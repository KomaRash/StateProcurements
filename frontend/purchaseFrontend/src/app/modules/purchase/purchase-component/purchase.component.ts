import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
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
  updatable:boolean=false;
  purchaseLots:PurchaseLot[]=[];
  constructor(public route: ActivatedRoute,private fb: FormBuilder,public purchaseService:PurchaseService){
    this.purchaseForm=this.fb.group({
      purchaseDescription:['',Validators.required],
      procedureName:['',Validators.required],
      date:['',Validators.required],
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

      });
    });
    this.purchaseForm.disable();
    this.updatable=this.purchase.purchaseStatus=="CreatePurchase"
  }

  saveSegment() {

  }

  update() {
      this.updatable=true;
      this.purchaseForm.enable();
  }

  confirmUpdate() {
    this.purchaseForm.disable();
  }
}
