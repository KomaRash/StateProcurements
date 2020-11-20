import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Purchase} from "../../../../models/Purchase";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable, of, Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {PurchaseService} from "../../../../services/purchase.service";


@Component({
  selector: 'app-purchase-info',
  templateUrl: './purchase-info.component.html',
  styleUrls: ['./purchase-info.component.css']
})
export class PurchaseInfoComponent implements OnInit {
  @Input() purchase: Purchase;
  @Output() purchaseChanged = new EventEmitter<Purchase>()
  @Input() enableEdit: boolean;

  purchaseForm: FormGroup

  constructor( private fb: FormBuilder) {
    this.purchaseForm = this.fb.group({
      purchaseDescription: ['', Validators.required],
      procedureName: ['', Validators.required],
      date: ['', Validators.required],
    })

  }

  ngOnInit(): void {

    this.purchaseForm.valueChanges.subscribe(() =>
      this.purchaseChanged.emit(this.purchase)

    )
    of(this.purchase).subscribe(purchase=>{
      this.purchaseForm.setValue({
        purchaseDescription: this.purchase.description,
        procedureName: this.purchase.purchaseInfo.procedureName,
        date: this.purchase.purchaseInfo.dateOfPurchase
      })
    })
    if(!this.enableEdit)
      this.purchaseForm.disable()
    else
      this.purchaseForm.enable();
  }
}

