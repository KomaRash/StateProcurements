import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {appConfig} from "../app.config";
import {Position} from "../models/Position";
import {Purchase} from "../models/Purchase";
import {Observable} from "rxjs";
import {OKRBProduct} from "../models/OKRBProduct";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  constructor(private http: HttpClient) { }
  getPurchaseList(): Observable<Array<Purchase>> {
    return this.http.get<Array<Purchase>>(appConfig.url+"purchases")

  }
  getPurchaseInfo(purchaseId: string) {
    return this.http.get<Purchase>(appConfig.url+"purchases/"+purchaseId)
  }

  getOKRB(okrb: OKRBProduct) {
    return okrb.section+'.'+
      okrb.productClass+'.'+
      okrb.subCategories+'.'+
      okrb.grouping
  }
}
