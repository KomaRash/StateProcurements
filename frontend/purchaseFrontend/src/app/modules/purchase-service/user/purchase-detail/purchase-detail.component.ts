import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PurchaseService} from "../../../../services/purchase.service";
import {Subscription} from "rxjs";
import {Purchase} from "../../../../models/Purchase";
import {Location} from '@angular/common';
@Component({
  selector: 'app-purchase-detail',
  templateUrl: './purchase-detail.component.html',
  styleUrls: ['./purchase-detail.component.css']
})
export class PurchaseDetailComponent implements OnInit {
  sub:Subscription;
  purchaseId:string
  purchase:Purchase
  upload:boolean
  constructor(public route: ActivatedRoute,
              public purchaseService:PurchaseService) {
    this.upload=false;
    this.sub = this.route.params.subscribe(params => {
      this.purchaseId =  params['id'];
      console.log(this.purchaseId)
    });
  }


  ngOnInit(): void {
    this.purchaseService.getPurchaseInfo(this.purchaseId).subscribe(purchase => {
      this.purchase=purchase;
      this.upload=true;
    });
    }

}
