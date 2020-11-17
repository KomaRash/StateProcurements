import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AdminComponent} from './admin.component';
import {HeaderNavBarComponent} from './header-nav-bar/header-nav-bar.component';
import {RouterModule, Routes} from "@angular/router";
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
import {PurchaseListComponent} from "../shared/purchase-list/purchase-list.component";
import {UserListComponent} from "./user-list/user-list.component";
import {AdminGuard} from "../../../guards/admin.guard";
import {BrowserModule} from "@angular/platform-browser";
import {SharedModule} from "../shared/shared.module";

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [AdminGuard],
    redirectTo: 'admin/users',
    pathMatch: 'full'
  },
  {
    path: 'admin/users',
    component: AdminComponent,
    canActivate: [AdminGuard],
    children: [
      {path: '', component: UserListComponent},
      {path: ':id', component: PurchaseListComponent},
    ]
  },
]

@NgModule({
  declarations: [
    AdminComponent,
    HeaderNavBarComponent],
  imports: [
    RouterModule.forRoot(routes),
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
  ]
})
export class AdminModule {
}
