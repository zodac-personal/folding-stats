#!/busybox sh

if [ "${DEPLOYMENT_TYPE}" = "production" ]; then
  /busybox wget --no-check-certificate --quiet --tries=1 --spider https://127.0.0.1:8443/folding/actuator/health || exit 1
else
  /busybox wget --quiet --tries=1 --spider http://127.0.0.1:8443/folding/actuator/health || exit 1
fi
