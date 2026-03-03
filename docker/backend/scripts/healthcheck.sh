#!/bin/sh

if [ "${DEPLOYMENT_TYPE}" = "production" ]; then
  /bin/wget --no-check-certificate --quiet --tries=1 --spider https://127.0.0.1:8443/folding/actuator/health || exit 1
else
  /bin/wget --quiet --tries=1 --spider http://127.0.0.1:8443/folding/actuator/health || exit 1
fi
