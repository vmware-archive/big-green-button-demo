import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { environment } from '../environments/environment';
import { VersionCheckService} from './version-check.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  locale = 'de';

  constructor(private titleService: Title, private versionCheckService: VersionCheckService) {
    const path: string = location.pathname.toString();
    if (path.startsWith('/en/')) {
      this.locale = 'en';
      // this is ugly -- Angular really needs to get this integrated into the il8n mechanisms
      titleService.setTitle('A Brief History of Soccer');
    } else if (path.startsWith('/nl/')) {
      this.locale = 'nl';
      // this is ugly -- Angular really needs to get this integrated into the il8n mechanisms
      titleService.setTitle('Voetbal: een beknopte samenvatting');
    }
  }

  onLocaleChange(value) {
    if (value == 'de') {
      window.location.href = '/';
    } else {
      window.location.href = '/' + value + '/';
    }
  }

  ngOnInit() {
    this.versionCheckService.initVersionCheck(environment.versionCheckURL, environment.refreshRate);  
  }
}
