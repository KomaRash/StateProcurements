import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {appConfig} from "../app.config";
import {DocumentInfo} from "../models/DocumentInfo";
import {Observable} from "rxjs";
import * as fileSaver from 'file-saver';
@Injectable({
  providedIn: 'root'
})
export class DocumentService {


  constructor(private http:HttpClient){

  }
  getDocumentsInfo(purchaseId:string): Observable<Array<DocumentInfo>> {
   return  this.http.get<Array<DocumentInfo>>(appConfig.url+`documents/purchases/${purchaseId}`)
  }
  downloadDocument(link:string){
    return  this.http.get<Response>(appConfig.url+`documents/${link}`).subscribe(data=> {

    })
  }
 /* getDocument(sourceLink:string):Observable<M>*/
}
