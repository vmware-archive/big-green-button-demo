#!/bin/bash
export TERM=${TERM:-dumb}

echo "Logging into CF"
cf login -a $CF_API -o $CF_ORG -s $CF_SPACE -u $CF_USER -p $CF_PASSWD

echo "Checking for blue service deployment"
cf check-route $CF_SERVICE_NAME-blue $CF_DOMAIN | grep 'does exist' &> /dev/null
if [ $? == 0 ]; then
	set -e
	echo "Adding blue route to service"
	cf map-route $CF_SERVICE_NAME-blue $CF_DOMAIN -n $CF_SERVICE_NAME
	echo "Removing green service route"
	cf unmap-route $CF_SERVICE_NAME $CF_DOMAIN -n $CF_SERVICE_NAME
	echo "Deleting green service"
	cf delete $CF_SERVICE_NAME -f
	echo "Renaming blue service to green"
	cf rename $CF_SERVICE_NAME-blue fussball-service
	echo "Removing blue service route"
	cf delete-route $CF_DOMAIN -n $CF_SERVICE_NAME-blue -f
else
	echo "No blue service route found; skipping"
fi
