import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderNavBarComponent} from "./header-nav-bar/header-nav-bar.component";
import {RouterModule, Routes} from "@angular/router";
import {PurchaseListComponent} from "../shared/purchase-list/purchase-list.component";
import {PurchaseDetailComponent} from "../shared/purchase-detail/purchase-detail.component";
import {UserComponent} from "./user.component";
import {BrowserModule} from "@angular/platform-browser";
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
import {UserGuard} from "../../../guards/user.guard";
import { CreatePurchaseComponent } from './create-purchase/create-purchase.component';
import {SharedModule} from "../shared/shared.module";

export const userRoutes: Routes = [
 {
    path: 'user',
    redirectTo: 'user/purchases',
    canActivate: [UserGuard],
    pathMatch: 'full'
  },
  {
    path: 'user/purchases',
    component: UserComponent,
    canActivate: [UserGuard],
    children: [
      {path: '', component: PurchaseListComponent},
      {path: 'create', component: CreatePurchaseComponent},
      {path: ':id', component: PurchaseDetailComponent},
    ]
  },
]

@NgModule({
  declarations: [
    HeaderNavBarComponent,
    UserComponent,
    CreatePurchaseComponent
  ],
  imports: [
    RouterModule.forRoot(userRoutes),
    CommonModule,
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
    SharedModule

  ],
})
export class UserModule {
}
