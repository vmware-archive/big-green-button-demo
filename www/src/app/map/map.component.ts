import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  loading = true;
  error = false;
  matches: any[];
  years: Array<Number>;
  currentYear: number;

  constructor(private _http: HttpClient) {
    this.currentYear = 2018;
    this.years = Array(147).fill(0).map((x,i)=>2018-i);
  }

  ngOnInit() {
    this.loadData();
  }

  setYear(year: number) {
    this.currentYear = year;
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.error = false;
    this._http.get(environment.apiPrefix + '/matches/search/findAllByYearOrderByDateDesc?projection=matchSummary&year=' + this.currentYear + '&size=200')
      .subscribe(data => {
        this.matches = data['_embedded']['matches'] ? data['_embedded']['matches'] : [];
        this.loading = false;
      }, error => {
        this.loading = false;
        this.error = true;
      });
  }
}
