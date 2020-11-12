import { Component, OnInit } from '@angular/core';
import {DocumentInfo} from "../../../../models/DocumentInfo";

@Component({
  selector: 'app-purchase-document-list',
  templateUrl: './purchase-document-list.component.html',
  styleUrls: ['./purchase-document-list.component.css']
})
export class PurchaseDocumentListComponent implements OnInit {
  documentList:DocumentInfo[];
  constructor() {
    this.documentList=[];
  }

  ngOnInit(): void {
  }

  download(sourceLink: string) {

  }
}
