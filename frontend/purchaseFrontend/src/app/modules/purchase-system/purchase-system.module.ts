import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PurchaseSystemComponent } from './purchase-system.component';
import {Route, Router, RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "../auth/login/login.component";
import {LoginGuardService} from "../../guards/login-guard.service";
import {AuthGuardService} from "../../guards/auth-guard.service";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppModule} from "../../app.module";
import {PurchaseHeaderComponent} from "../purchase-header/purchase-header.component";


export const routes: Routes = [
  {
    path: 'purchases',
    component: PurchaseSystemComponent,
    canActivate: [AuthGuardService]
  }
];

@NgModule({
  declarations: [
    PurchaseSystemComponent,
    PurchaseHeaderComponent],
  imports: [
    RouterModule.forRoot(routes),
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
  ]
})
export class PurchaseSystemModule { }
