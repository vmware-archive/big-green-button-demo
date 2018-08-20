import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatchesComponent } from "../matches/matches.component";
import { TeamsComponent } from "../teams/teams.component";
import { MapComponent } from "../map/map.component";

const routes: Routes = [
  { path: '',
    redirectTo: 'map',
    pathMatch: 'full'
  },
  {
    path: 'map',
    component: MapComponent
  },
  {
    path: 'matches',
    component: MatchesComponent
  },
  {
    path: 'teams',
    component: TeamsComponent
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: []
})
export class AppRoutingModule { }
