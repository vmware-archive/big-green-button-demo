#!/bin/bash
export TERM=${TERM:-dumb}

echo "Logging into CF"
cf login -a $CF_API -o $CF_ORG -s $CF_SPACE -u $CF_USER -p $CF_PASSWD

echo "Checking for blue www deployment"
cf check-route $CF_WWW_NAME_BLUE $CF_DOMAIN | grep 'does exist' &> /dev/null
if [ $? == 0 ]; then
	set -e
	echo "Adding blue route to www"
	cf map-route $CF_WWW_NAME_BLUE $CF_DOMAIN -n $CF_WWW_NAME_GREEN
	echo "Removing green www route"
	cf unmap-route $CF_WWW_NAME $CF_DOMAIN -n $CF_WWW_NAME_GREEN
	echo "Deleting green www"
	cf delete $CF_WWW_NAME_GREEN -f
	echo "Renaming blue www to green"
	cf rename $CF_WWW_NAME_BLUE $CF_WWW_NAME_GREEN
	echo "Removing blue www route"
	cf delete-route $CF_DOMAIN -n $CF_WWW_NAME_BLUE -f
else
	echo "No blue www route found; skipping"
fi
