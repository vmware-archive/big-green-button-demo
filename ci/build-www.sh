#!/bin/sh 
cd big-green-www/www
echo "export const environment = { production: true, apiPrefix: '$API_PREFIX', mapsApiKey: '$GOOGLE_MAPS_API_KEY' };" > src/environments/environment.prod.ts
npm install
ng build --source-map=false --configuration=production
ng build --configuration=production-en
ng build --configuration=production-nl
cp -rf dist/www/* ../../build-output2/
mkdir ../../build-output2/en
cp -rf dist/www-en/* ../../build-output2/en/
mkdir ../../build-output2/nl
cp -rf dist/www-nl/* ../../build-output2/nl/
