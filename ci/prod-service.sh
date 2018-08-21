#!/bin/bash
export TERM=${TERM:-dumb}

echo "Logging into CF"
cf login -a $CF_API -o $CF_ORG -s $CF_SPACE -u $CF_USER -p $CF_PASSWD

echo "Checking for pre-prod service deployment"
cf check-route $CF_SERVICE_NAME-blue $CF_DOMAIN | grep 'does exist' &> /dev/null
if [ $? == 0 ]; then
	set -e
	echo "Adding pre-prod route to service"
	cf map-route $CF_SERVICE_NAME-blue $CF_DOMAIN -n $CF_SERVICE_NAME
	echo "Checking for prod service deployment"
	cf check-route $CF_SERVICE_NAME $CF_DOMAIN | grep 'does exist' &> /dev/null
	if [ $? == 0 ]; then
		echo "Removing prod service route"
		cf unmap-route $CF_SERVICE_NAME $CF_DOMAIN -n $CF_SERVICE_NAME
		echo "Deleting prod service"
		cf delete $CF_SERVICE_NAME -f
	fi
	echo "Renaming pre-prod service to prod"
	cf rename $CF_SERVICE_NAME-blue fussball-service
	echo "Removing pre-prod service route"
	cf delete-route $CF_DOMAIN -n $CF_SERVICE_NAME-blue -f
else
	echo "No pre-prod service route found; skipping"
fi
