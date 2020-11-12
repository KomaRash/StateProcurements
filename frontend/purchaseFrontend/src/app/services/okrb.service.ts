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
  getLength(searchField:string): Observable<number> {
    const href = appConfig.url+'okrb';
    const requestUrl =
      `${href}/length?searchField=${searchField}`;
    return this.http.get<number>(requestUrl) ;
  }
  getOKRBList(pageIndex: number,size:number,field:string): Observable<Array<OKRBProduct>> {
    const href = appConfig.url+'okrb';
    const requestUrl =
      `${href}?page=${pageIndex}&pageSize=${size}`;
    return this.http.get<Array<OKRBProduct>>(requestUrl) ;
  }
}
