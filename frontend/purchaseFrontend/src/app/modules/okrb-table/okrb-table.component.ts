import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {OKRBProduct} from "../../models/OKRBProduct";
import {OkrbService} from "../../services/okrb.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {merge, of} from "rxjs";
import {catchError, delay, map, startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-okrb-table',
  templateUrl: './okrb-table.component.html',
  styleUrls: ['./okrb-table.component.css']
})
export class OkrbTableComponent implements AfterViewInit{
  dataSource:MatTableDataSource<OKRBProduct>=new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  resultsLength = 0;
  pageSize=20;
  isLoadingResults = false;
  isRateLimitReached = false;
  columnsToDisplay: string[] = ['section', 'class', 'subCategories', 'groupings','name'];

  constructor(public okrbService:OkrbService) {

  }
  ngAfterViewInit(){
    merge(this.sort.sortChange, this.paginator.page).
      pipe(
      startWith({}), delay(0),switchMap(() => {
        this.isLoadingResults = true;
        return this.okrbService.getOKRBList(
           this.paginator.pageIndex,
            this.pageSize
        )
      }),
      map(data => {
        // Flip flag to show that loading has finished.
        this.isLoadingResults = false;
        this.isRateLimitReached = false;
        this.resultsLength = data.length;

        return data;
      }),
      catchError(() => {
        this.isLoadingResults = false;
        this.isRateLimitReached = true;
        return of([]);
      })
    ).subscribe(data => {
      return this.dataSource.data = data;
    });

  }


}

