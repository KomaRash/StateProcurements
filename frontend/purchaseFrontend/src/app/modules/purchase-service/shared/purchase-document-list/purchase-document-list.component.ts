import {Component, Input, OnInit} from '@angular/core';
import {DocumentInfo} from "../../../../models/DocumentInfo";
import {DocumentService} from "../../../../services/document.service";

@Component({
  selector: 'app-purchase-document-list',
  templateUrl: './purchase-document-list.component.html',
  styleUrls: ['./purchase-document-list.component.css']
})
export class PurchaseDocumentListComponent implements OnInit {
  @Input()documentList:DocumentInfo[];

  constructor(private documentService:DocumentService) {
  }

  ngOnInit(): void {
  }

  download(sourceLink: string) {
  }
}
