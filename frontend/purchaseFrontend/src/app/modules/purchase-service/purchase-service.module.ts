import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
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
import {PurchaseInfoComponent} from './shared/purchase-info/purchase-info.component';
import {PurchaseDetailComponent} from './shared/purchase-detail/purchase-detail.component';
import {MatButtonModule} from "@angular/material/button";
import {PurchaseLotsTableComponent} from './shared/purchase-lots-table/purchase-lots-table.component';
import {PurchaseDocumentListComponent} from './shared/purchase-document-list/purchase-document-list.component';
import { UserListComponent } from './admin/user-list/user-list.component';
import { PurchaseListComponent } from './shared/purchase-list/purchase-list.component';
import { PurchaseLotDetailComponent } from './shared/purchase-lot-detail/purchase-lot-detail.component';
import {UserModule} from "./user/user.module";
import {UserComponent} from "./user/user.component";
import {AdminComponent} from "./admin/admin.component";
import {AdminGuard} from "../../guards/admin.guard";
import {UserGuard} from "../../guards/user.guard";


export const routes: Routes = [
  {
    path: 'user',
    component:PurchaseServiceComponent,
    canActivate: [UserGuard],
    children: [
      {path: '', component: UserComponent},
    ]
  },
  {
    path: 'admin',
    component:PurchaseServiceComponent,
    canActivate: [AdminGuard],
    children: [
      {path: '', component: AdminComponent},
    ]
  }

]

@NgModule({
  declarations: [
    PurchaseServiceComponent,
    PurchaseInfoComponent,
    PurchaseDetailComponent,
    PurchaseLotsTableComponent,
    PurchaseDocumentListComponent,
    UserListComponent,
    PurchaseListComponent,
    PurchaseLotDetailComponent,

  ],
  exports: [
    PurchaseInfoComponent,
    PurchaseServiceComponent,
    PurchaseInfoComponent,
    PurchaseDetailComponent,
    PurchaseLotsTableComponent,
    PurchaseDocumentListComponent,
    UserListComponent,
    PurchaseListComponent,
    PurchaseLotDetailComponent,

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
    UserModule,
  ]
})
export class PurchaseServiceModule {
}
