import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.css']
})
export class TeamsComponent implements OnInit {
  loading = true;
  error = false;
  teams: any[];
  pages: any[];
  currentPage: number;

  constructor(private _http: HttpClient, private route: ActivatedRoute) {
    this.currentPage = 1;
  }

  ngOnInit() {
    this.loadData();
  }

  setPage(page: number) {
    this.currentPage = page;
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.error = false;
    const p: number = this.currentPage - 1;
    this._http.get(environment.apiPrefix + '/teams?page=' + p)
      .subscribe(data => {
        this.teams = data['_embedded']['teams'];
        this.currentPage = data['page']['number'] + 1;
        this.pages = Array(data['page']['totalPages']).fill(0).map((x, i) => i+1);
        this.loading = false;
      }, error => {
        this.loading = false;
        this.error = true;
      });
  }
}
