import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./login/login.component";
import {LoginGuardService} from "../../guards/login-guard.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginGuardService]
  }
];
@NgModule({
  declarations: [LoginComponent],
  imports: [
    RouterModule.forRoot(routes),
    CommonModule,

    FormsModule,
    ReactiveFormsModule
  ],
  providers:[AuthService,LoginGuardService],
  bootstrap:[LoginComponent]
})
export class AuthModule { }
