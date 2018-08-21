#!/bin/bash
export TERM=${TERM:-dumb}
pushd big-green-service/fussball-service
gradle -s --no-daemon build
cp build/libs/*.jar ../../build-output/
popd
find .
