import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PurchaseLot} from "../../../../models/PurchaseLot";
import {OKRBProduct} from "../../../../models/OKRBProduct";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-purchase-lot-detail',
  templateUrl: './purchase-lot-detail.component.html',
  styleUrls: ['./purchase-lot-detail.component.css']
})
export class PurchaseLotDetailComponent implements OnInit {
  form:FormGroup
  constructor(
    public dialogRef: MatDialogRef<PurchaseLotDetailComponent>,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: PurchaseLot) {
    this.form=this.fb.group({
      name: [this.data.name, Validators.required],
      amount:[this.data.amount,Validators.required],
      deadline: [this.data.deadline, Validators.required],
    });
  }
  public expandedElement: OKRBProduct

  onNoClick(): void {
    this.data=undefined;
    this.dialogRef.close();

  }

  ngOnInit(): void {
  }

  onChanged(increased:OKRBProduct){
    this.expandedElement=increased;
    this.data.okrb=increased;
    console.log(increased)
  }

  okClick() {
    if(this.form.valid)
    this.dialogRef.close(this.data)
  }
}
