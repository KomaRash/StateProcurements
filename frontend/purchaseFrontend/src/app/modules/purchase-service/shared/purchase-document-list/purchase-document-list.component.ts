import {Component, Input, OnInit} from '@angular/core';
import {DocumentInfo} from "../../../../models/DocumentInfo";
import {DocumentService} from "../../../../services/document.service";
import {appConfig} from "../../../../app.config";
import {fromEvent, Subject} from "rxjs";
import {finalize, first, mergeMap, takeUntil} from "rxjs/operators";
import {HttpEvent, HttpEventType} from "@angular/common/http";
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-purchase-document-list',
  templateUrl: './purchase-document-list.component.html',
  styleUrls: ['./purchase-document-list.component.css']
})
export class PurchaseDocumentListComponent implements OnInit {
  @Input() documentList: DocumentInfo[];
  @Input() purchaseId: number;
  private destroy$ = new Subject<void>();

  constructor(private documentService: DocumentService) {
  }

  ngOnInit(): void {
  }

  download(doc:DocumentInfo) {
    let href = appConfig.url + 'documents/' + doc.sourceLink;
    this.documentService.downloadDocument(href).subscribe(response=>{
      saveAs(response,`${doc.documentName}.${doc.extensions}`)
    })
  }

  handleFileInput() {
    let fileInput = document.createElement('input');
    fileInput.type = 'file';
    fromEvent(fileInput, 'change')
      .pipe(
        first(),
        mergeMap(event => {
          const target = event.target as HTMLInputElement;
          const selectedFile = target.files[0];
          // formData обязательно в 2 строчки
          let uploadData = new FormData();
          uploadData.append('upload_file', selectedFile, selectedFile.name);
          return this.documentService.uploadFile(uploadData, this.purchaseId)
        }),
        finalize(() => {
          // должен быть удален, т.к. счетчик ссылок обнулится
          fileInput = null;
          console.log('fileInput = null');
        }),
        takeUntil(this.destroy$)
      )
      .subscribe(
        (event: HttpEvent<Array<DocumentInfo>>) => {
          // console.log(event);
          switch (event.type) {
            case HttpEventType.Sent:
              console.log('Request sent!');
              break;
            case HttpEventType.ResponseHeader:
              console.log('Response header received!');
              break;
            case HttpEventType.UploadProgress:
              const kbLoaded = Math.round(event.loaded / 1024 / 1024);
              const percent = Math.round((event.loaded * 100) / event.total);
              console.log(
                `Upload in progress! ${kbLoaded}Mb loaded (${percent}%)`
              );
              break;
            case HttpEventType.Response:
              console.log('Done!', event.body);
              this.documentList.push(event.body[0]);

          }
        },
        () => console.log('Upload error'),
        () => console.log('Upload complete')
      );
    fileInput.click();
  }
}
