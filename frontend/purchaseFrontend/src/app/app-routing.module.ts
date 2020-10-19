import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthModule} from "./modules/auth/auth.module";
import {LoginGuardService} from "./guards/login-guard.service";

const routes:  Routes = [
  { path: '',
    redirectTo:'login',
    pathMatch: 'full',
    },
  {
    path:'**',
    redirectTo:'login',
  }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes,{enableTracing:true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
