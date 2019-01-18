import { Observable } from "rxjs/Rx";
import 'rxjs/add/operator/first';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class VersionCheckService {
    // this will be replaced by actual hash post-build.js
    private currentHash = '{{POST_BUILD_ENTERS_HASH_HERE}}';

    constructor(private http: HttpClient) {}

    /**
     * Checks in every set frequency the version of frontend application
     * @param url
     * @param {number} frequency - in milliseconds, defaults to 1 minute
     */
    public initVersionCheck(protocol, domain, appNameProd, versionCheckPath, refreshRate) {
        // check if this app is running in prod
        const url = window.location.host;
        const host: string = url.split(/[:.]/)[0];
        const isProd = (appNameProd === host);
        console.log('I appear to be running in', isProd ? 'prod' : 'non-prod');
        const prodUrl = protocol + '://' + appNameProd + '.' + domain + versionCheckPath;

        // start the version polling
        console.log('Version checking polling starting with refresh rate of', refreshRate, 'seconds');
        setInterval(() => {
            this.checkVersion(versionCheckPath, prodUrl, isProd);
        }, Number(refreshRate) * 1000);
    }

    /**
     * Will do the call and check if the hash has changed or not
     * @param url
     */
    private checkVersion(myUrl, prodUrl, isProd) {
        // request version of this application and auto-refresh if it has changed
        console.log('Checking my version:', myUrl, this.currentHash);
        this.http.get(myUrl + '?t=' + new Date().getTime()) // timestamp these requests to invalidate caches
            .first()
            .subscribe(
                (response: any) => {
                    const hash = response.hash;
                    const hashChanged = this.hasHashChanged(this.currentHash.toString(), hash.toString());

                    // If new version, do something
                    if (hashChanged) {
                        window.location.href = window.location.protocol + "//" + window.location.host + "/";
                    }
                    // store the new hash so we wouldn't trigger versionChange again
                    // only necessary in case you did not force refresh
                    this.currentHash = hash;
                },
                (err) => {
                    console.error(err, 'Could not get version');
                }
            );

        // if not in prod, check version of prod app
        if (!isProd) {
            console.log('Checking prod version:', prodUrl, this.currentHash);
            this.http.get(prodUrl + '?t=' + new Date().getTime()) // timestamp these requests to invalidate caches
                .first()
                .subscribe(
                    (response: any) => {
                        const hash = response.hash;
                        const hashChanged = this.hasHashChanged(this.currentHash.toString(), hash.toString());

                        // If new version, do something
                        if (hashChanged) {
                            console.log('I appear to be out of sync with prod!');
                        }
                        // store the new hash so we wouldn't trigger versionChange again
                        // only necessary in case you did not force refresh
                        this.currentHash = hash;
                    },
                    (err) => {
                        console.error(err, 'Could not get version');
                    }
                );
        }
    }

    /**
     * Checks if hash has changed.
     * This file has the JS hash, if it is a different one than in the version.json
     * we are dealing with version change
     * @param currentHash
     * @param newHash
     * @returns {boolean}
     */
    private hasHashChanged(currentHash, newHash) {
        if (!currentHash || currentHash == '{{POST_BUILD_ENTERS_HASH_HERE}}') {
            return false;
        }

        return currentHash !== newHash;
    }
}
