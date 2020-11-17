import {Component, Input, OnInit} from '@angular/core';
import {DocumentInfo} from "../../../../models/DocumentInfo";
import {DocumentService} from "../../../../services/document.service";
import {appConfig} from "../../../../app.config";

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
    var link = document.createElement('a');
    link.href = appConfig.url+'documents/' + sourceLink;
    link.download = sourceLink;
    console.log(link)
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
