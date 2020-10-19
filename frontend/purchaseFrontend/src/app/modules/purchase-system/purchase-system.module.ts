import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PurchaseSystemComponent } from './purchase-system.component';
import {Route, Routes} from "@angular/router";
import {LoginComponent} from "../auth/login/login.component";
import {LoginGuardService} from "../../guards/login-guard.service";
import {AuthGuardService} from "../../guards/auth-guard.service";


export const routes: Routes = [
  {
    path: 'purchases',
    component: PurchaseSystemComponent,
    canActivate: [AuthGuardService]
  }
];

@NgModule({
  declarations: [PurchaseSystemComponent],
  imports: [
    CommonModule
  ]
})
export class PurchaseSystemModule { }
