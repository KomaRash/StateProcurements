import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {appConfig} from "../app.config";
import {Observable} from "rxjs";
import {OKRBProduct} from "../models/OKRBProduct";

@Injectable({
  providedIn: 'root'
})
export class OkrbService {

  constructor(public http:HttpClient) { }

  getOKRBList(pageIndex: number,size:number): Observable<Array<OKRBProduct>> {
    const href = appConfig.url+'okrb';
    const requestUrl =
      `${href}?page=${pageIndex}&pageSize=${size}`;
    return this.http.get<Array<OKRBProduct>>(requestUrl) ;
  }
}
