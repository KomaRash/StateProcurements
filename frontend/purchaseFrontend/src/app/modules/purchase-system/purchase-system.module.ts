import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PurchaseSystemComponent } from './purchase-system.component';
import { RouterModule, Routes} from "@angular/router";
import {AuthGuardService} from "../../guards/auth-guard.service";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {PurchaseHeaderComponent} from "../purchase-header/purchase-header.component";
import {ExpansionPurchaseComponent} from "../expansion-purchase/expansion-purchase.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {CreatePurchaseComponent} from "../create-purchase/create-purchase.component";
import {PurchaseComponent} from "../purchase/purchase-component/purchase.component";
import {AppRoutingModule} from "../../app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {OkrbTableComponent} from "../okrb-table/okrb-table.component";
import {MatTableModule} from "@angular/material/table";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatInputModule} from "@angular/material/input";
import {MatFormFieldModule} from "@angular/material/form-field";


export const routes: Routes = [
  {
    path: 'purchases',
    component: PurchaseSystemComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '', component: ExpansionPurchaseComponent },
    ]
  },
  {
    path: 'create',
    component: PurchaseSystemComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '', component: CreatePurchaseComponent }
    ]
  },
  {
    path: 'purchases/:id',
    component: PurchaseSystemComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '', component: PurchaseComponent }
    ]

  }
];

@NgModule({
    declarations: [
      CreatePurchaseComponent,
      OkrbTableComponent,
        PurchaseSystemComponent,
        PurchaseHeaderComponent,
        ExpansionPurchaseComponent,
        PurchaseComponent,
    ],
  imports: [
    RouterModule.forRoot(routes),
    CommonModule,
    BrowserModule,
    FormsModule,
    CommonModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatExpansionModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  exports: [MatFormFieldModule, MatInputModule, OkrbTableComponent]
})
export class PurchaseSystemModule { }
