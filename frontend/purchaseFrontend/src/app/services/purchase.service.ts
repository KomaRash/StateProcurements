import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {appConfig} from "../app.config";
import {Purchase} from "../models/Purchase";
import {Observable} from "rxjs";
import {PurchaseLot} from "../models/PurchaseLot";


@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  constructor(private http: HttpClient) {
  }

  getPurchaseList(): Observable<Array<Purchase>> {
    return this.http.get<Array<Purchase>>(appConfig.url + "purchases")
  }

  savePurchase(purchase: Purchase): Observable<Purchase> {
    return this.http.post<Purchase>(appConfig.url + "purchases", purchase.toJson())
  }

  savePurchaseLots(purchase: Purchase) {
    return this.http.post<Purchase>(
      appConfig.url + `purchases/${purchase.purchaseId}/lots`,
      JSON.stringify(purchase.purchaseLots)
    )
  }
  confirmPurchaseCreate(purchase: Purchase) {
    return this.http.put<Purchase>(appConfig.url + `purchases/${purchase.purchaseId}/confirm`,purchase.toJson())
  }
  getPurchaseInfo(purchaseId: string) {
    return this.http.get<Purchase>(appConfig.url + "purchases/" + purchaseId)
  }


}
