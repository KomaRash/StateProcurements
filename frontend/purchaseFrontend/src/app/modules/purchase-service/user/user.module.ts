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

const routes: Routes = [
  {
    path: '',
    redirectTo: 'purchases',
  },
  {
    path: 'purchases',
    component: UserComponent,
    canActivate: [UserGuard],
    children: [
      {path: '', component: PurchaseListComponent},
      {path: ':id', component: PurchaseDetailComponent}
    ]
  },
]

@NgModule({
  declarations: [
    HeaderNavBarComponent,
    UserComponent
  ],

  imports: [
    RouterModule.forChild(routes),
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
  ],
})
export class UserModule {
}
