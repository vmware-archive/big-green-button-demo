#!/bin/bash
export TERM=${TERM:-dumb}

echo "Logging into CF"
cf login -a $CF_API -o $CF_ORG -s $CF_SPACE -u $CF_USER -p $CF_PASSWD

echo "Checking for pre-prod www deployment"
cf check-route $CF_WWW_NAME_BLUE $CF_DOMAIN | grep 'does exist' &> /dev/null
if [ $? == 0 ]; then
	set -e
	echo "Adding pre-prod route to prod"
	cf map-route $CF_WWW_NAME_BLUE $CF_DOMAIN -n $CF_WWW_NAME_GREEN
	set +e
	cf app $CF_WWW_NAME_GREEN | grep 'state' &> /dev/null
	if [ $? == 0 ]; then
		set -e
		echo "Deleting prod app"
		cf delete $CF_WWW_NAME_GREEN -f
	fi
	set -e
	echo "Renaming pre-prod app to prod"
	cf rename $CF_WWW_NAME_BLUE $CF_WWW_NAME_GREEN
	echo "Removing pre-prod route"
	cf delete-route $CF_DOMAIN -n $CF_WWW_NAME_BLUE -f
else
	echo "No pre-prod www route found; skipping"
fi
