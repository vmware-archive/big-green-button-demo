import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-matches',
  templateUrl: './matches.component.html',
  styleUrls: ['./matches.component.css']
})
export class MatchesComponent implements OnInit {
  loading = true;
  error = false;
  matches: any[];
  years: Array<Number>;
  pages: any[];
  currentPage: number;
  currentYear: number;

  constructor(private _http: HttpClient, private route: ActivatedRoute) {
    this.years = Array(147).fill(0).map((x,i)=>2018-i);
    this.currentYear = 2018;
    this.currentPage = 1;
  }

  ngOnInit() {
    this.loadData();
  }

  setYear(year: number) {
    this.currentYear = year;
    this.currentPage = 1;
    this.loadData();
  }

  setPage(page: number) {
    this.currentPage = page;
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.error = false;
    const url = environment.apiPrefix + '/matches/search/findAllByYearOrderByDateDesc?year=' + this.currentYear + '&projection=matchSummary&page=' + (this.currentPage - 1);
    this._http.get(url)
      .subscribe(data => {
        this.matches = data['_embedded']['matches'] ? data['_embedded']['matches'] : [];
        this.currentPage = data['page']['number'] + 1;
        this.pages = Array(data['page']['totalPages']).fill(0).map((x, i) => i+1);
        this.loading = false;
      }, error => {
        this.loading = false;
        this.error = true;
      });
  }

}
