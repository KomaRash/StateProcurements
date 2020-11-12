import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {AuthGuardService} from "../../guards/auth-guard.service";
import {ExpansionPurchaseComponent} from "../expansion-purchase/expansion-purchase.component";
import { HeaderNavBarComponent } from './header-nav-bar/header-nav-bar.component';
import {PurchaseServiceComponent} from "./purchase-service.component";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "../../app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTableModule} from "@angular/material/table";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatIconModule} from "@angular/material/icon";
import { PurchaseInfoComponent } from './shared/purchase-info/purchase-info.component';
import { PurchaseDetailComponent } from './user/purchase-detail/purchase-detail.component';
import {MatButtonModule} from "@angular/material/button";
import { PurchaseLotsTableComponent } from './shared/purchase-lots-table/purchase-lots-table.component';
import { PurchaseDocumentListComponent } from './shared/purchase-document-list/purchase-document-list.component';


export const routes: Routes = [
  {
    path: 'purchases',
    component: PurchaseServiceComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '', component: ExpansionPurchaseComponent },
    ]
  },
  {
    path: 'purchases/:id',
    component: PurchaseServiceComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '', component: PurchaseDetailComponent }
    ]

  }]
@NgModule({
  declarations: [
    HeaderNavBarComponent,
    ExpansionPurchaseComponent,
    PurchaseServiceComponent,
    PurchaseInfoComponent,
    PurchaseDetailComponent,
    PurchaseLotsTableComponent,
    PurchaseDocumentListComponent
  ],
  exports: [
    HeaderNavBarComponent,
    PurchaseInfoComponent,
    ExpansionPurchaseComponent
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
    MatIconModule,
    MatButtonModule,
  ]
})
export class PurchaseServiceModule { }
