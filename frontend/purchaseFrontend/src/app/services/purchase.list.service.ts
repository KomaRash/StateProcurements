import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {appConfig} from "../app.config";
import {Position} from "../models/Position";
import {Purchase} from "../models/Purchase";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PurchaseListService {

  constructor(private http: HttpClient) { }
  getPurchaseList(): Observable<Array<Purchase>> {
    return this.http.get<Array<Purchase>>(appConfig.url+"purchases")

  }
}
