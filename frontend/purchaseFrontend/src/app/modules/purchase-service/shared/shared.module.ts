import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PurchaseInfoComponent} from "./purchase-info/purchase-info.component";
import {PurchaseDetailComponent} from "./purchase-detail/purchase-detail.component";
import {PurchaseLotsTableComponent} from "./purchase-lots-table/purchase-lots-table.component";
import {PurchaseDocumentListComponent} from "./purchase-document-list/purchase-document-list.component";
import {UserListComponent} from "../admin/user-list/user-list.component";
import {PurchaseListComponent} from "./purchase-list/purchase-list.component";
import {PurchaseLotDetailComponent} from "./purchase-lot-detail/purchase-lot-detail.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "../../../app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTableModule} from "@angular/material/table";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from "@angular/material/dialog";
import {OkrbTableComponent} from "./okrb-table/okrb-table.component";
import {MatInputModule} from "@angular/material/input";


@NgModule({
  declarations: [
    PurchaseInfoComponent,
    PurchaseDetailComponent,
    PurchaseLotsTableComponent,
    PurchaseDocumentListComponent,
    UserListComponent,
    PurchaseListComponent,
    PurchaseLotDetailComponent,
    OkrbTableComponent,
    OkrbTableComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatExpansionModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
  ],
  exports: [
    PurchaseInfoComponent,
    PurchaseDetailComponent,
    PurchaseLotsTableComponent,
    PurchaseDocumentListComponent,
    UserListComponent,
    PurchaseListComponent,
    PurchaseLotDetailComponent,
  ],
  providers: [
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}}
  ]
})
export class SharedModule {
}
