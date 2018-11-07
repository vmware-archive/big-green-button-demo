import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { environment } from '../environments/environment';

import { AppRoutingModule } from "./app-routing/app-routing.module";
import { AgmCoreModule } from '@agm/core';
import { MatchesComponent } from './matches/matches.component';
import { TeamsComponent } from './teams/teams.component';
import { MapComponent } from './map/map.component';
import { LoaderComponent } from './loader/loader.component';
import { ErrorAlertComponent } from './error-alert/error-alert.component';
import { VersionCheckService} from './version-check.service';

import localeDe from '@angular/common/locales/de';
import localeNl from '@angular/common/locales/nl';

registerLocaleData(localeDe, 'de');
registerLocaleData(localeNl, 'nl');

@NgModule({
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
  	AgmCoreModule.forRoot({
      apiKey: environment.mapsApiKey
    })
  ],
  providers: [
    Title,
    VersionCheckService
  ],
  declarations: [ AppComponent, MatchesComponent, TeamsComponent, MapComponent, LoaderComponent, ErrorAlertComponent ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
