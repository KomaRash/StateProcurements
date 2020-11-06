import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {MatExpansionModule} from '@angular/material/expansion';
import {AuthModule} from "./modules/auth/auth.module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {PurchaseSystemModule} from "./modules/purchase-system/purchase-system.module";
import {AuthInterceptor} from "./services/auth.interceptor";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [

    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    AuthModule,
    PurchaseSystemModule,
    AppRoutingModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule {
}
