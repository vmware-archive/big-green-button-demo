#!/bin/bash
export TERM=${TERM:-dumb}
cd big-green-service/fussball-service
gradle -s --no-daemon test
