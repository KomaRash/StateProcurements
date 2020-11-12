import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {appConfig} from "../app.config";
import {DocumentInfo} from "../models/DocumentInfo";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DocumentService {


  constructor(public http:HttpClient){

  }
  getDocumentsInfo(purchaseId:number): Observable<Array<DocumentInfo>> {
   return  this.http.get<Array<DocumentInfo>>(appConfig.url+`purchases/${purchaseId}/documents`)
  }
}
