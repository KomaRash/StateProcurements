import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PurchaseService} from "../../../../services/purchase.service";
import {Observable, Subscription, forkJoin } from "rxjs";
import {Purchase} from "../../../../models/Purchase";
import {DocumentService} from "../../../../services/document.service";
import {DocumentInfo} from "../../../../models/DocumentInfo";
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
  documentInfoList: DocumentInfo[];
  constructor(public route: ActivatedRoute,
              public purchaseService:PurchaseService,
              public documentService:DocumentService) {
    this.upload=false;
    this.sub = this.route.params.subscribe(params => {
      this.purchaseId =  params['id'];
      console.log(this.purchaseId)
    });
  }


  ngOnInit(): void {
    let purchaseInfo=this.purchaseService.getPurchaseInfo(this.purchaseId)
    let documentInfo=this.documentService.getDocumentsInfo(this.purchaseId)
    forkJoin([purchaseInfo,documentInfo])
      .subscribe(result => {
        this.purchase=result[0];
        this.documentInfoList=result[1];
      this.upload=true;
    });
    }

}
