import {AfterViewInit, Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {OKRBProduct} from "../../../../models/OKRBProduct";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {OkrbService} from "../../../../services/okrb.service";
import {merge, of} from "rxjs";
import {catchError, delay, map, startWith, switchMap} from "rxjs/operators";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-okrb-table',
  templateUrl: './okrb-table.component.html',
  styleUrls: ['./okrb-table.component.css']
})
export class OkrbTableComponent implements AfterViewInit {
  dataSource: MatTableDataSource<OKRBProduct> = new MatTableDataSource();
  resultsLength = 150;
  pageSize = 2;
  isLoadingResults = false;
  isRateLimitReached = false;
  columnsToDisplay: string[] = ['section', 'class', 'subCategories', 'groupings', 'name'];
  expandedElement: OKRBProduct;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  searchLine: string = "";
  @Output() searchField = new EventEmitter<string>()
  @Output() onChanged;
  formGroup: FormGroup;

  setOKRB(increased: OKRBProduct) {
    this.expandedElement = increased;
    this.onChanged.emit(increased);
  }


  constructor(private okrbService: OkrbService, private formBuilder: FormBuilder) {
    this.onChanged = new EventEmitter<OKRBProduct>();
    this.formGroup = formBuilder.group({filter: ['']});

  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page, this.searchField).pipe(
      startWith({}), delay(0), switchMap(() => {
        this.isLoadingResults = true;
        return this.okrbService.getOKRBList(
          this.paginator.pageIndex,
          this.pageSize, this.searchLine
        )
      }),
      map(data => {
        // Flip flag to show that loading has finished.
        this.isLoadingResults = false;
        this.isRateLimitReached = false;

        return data;
      }),
      catchError(() => {
        this.isLoadingResults = false;
        this.isRateLimitReached = true;
        return of([]);
      }),
      map(data => {
        this.okrbService.getLength(this.searchLine).subscribe(x => {
          this.resultsLength = x
        })
        return data
      })
    ).subscribe(data => {

      return this.dataSource.data = data;
    });

  }

  onSearch($event: any) {
    this.searchLine = this.formGroup.get("filter").value
    this.searchField.emit()
  }
}
