import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatExpansionModule} from '@angular/material/expansion';
import {AuthModule} from "./modules/auth/auth.module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./services/auth.interceptor";
import { CreateLotComponent } from './modules/create-purchase/create-lot/create-lot.component';
import {FormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {PurchaseServiceModule} from "./modules/purchase-service/purchase-service.module";

@NgModule({
  declarations: [
    AppComponent,
    CreateLotComponent,
  ],
  imports: [

    MatInputModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    AuthModule,
    AppRoutingModule,
    FormsModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule,
    PurchaseServiceModule
  ],
  providers: [
  {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
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
