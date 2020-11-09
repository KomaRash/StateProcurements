import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PurchaseLot} from "../../../models/PurchaseLot";
import {OKRBProduct} from "../../../models/OKRBProduct";
import {catchError} from "rxjs/operators";
import {error} from "@angular/compiler/src/util";

@Component({
  selector: 'app-create-lot',
  templateUrl: './create-lot.component.html',
  styleUrls: ['./create-lot.component.css']
})
export class CreateLotComponent implements OnInit {


  constructor(
    public dialogRef: MatDialogRef<CreateLotComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PurchaseLot) {}
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
}
