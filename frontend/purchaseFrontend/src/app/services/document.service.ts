import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent} from "@angular/common/http";
import {appConfig} from "../app.config";
import {DocumentInfo} from "../models/DocumentInfo";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DocumentService {


  private _docRoute: String = "documents/purchases/"

  constructor(private http: HttpClient) {

  }

  getDocumentsInfo(purchaseId: string): Observable<Array<DocumentInfo>> {
    return this.http.get<Array<DocumentInfo>>(appConfig.url + `${this._docRoute}${purchaseId}`)
  }

  downloadDocument = (link: string) => this.http.get(link,{ responseType: 'blob' });

  /* getDocument(sourceLink:string):Observable<M>*/
  uploadFile(formData: FormData, purchaseId: number): Observable<HttpEvent<Array<DocumentInfo>>> {
    return this.http.post<Array<DocumentInfo>>(`${appConfig.url}${(this._docRoute)}${purchaseId}`, formData, {
      reportProgress: true,
      observe: 'events',
    });

  }
}
