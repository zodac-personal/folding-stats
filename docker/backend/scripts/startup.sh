#!/bin/sh

instance_type="test"
ssl_trust_all="-Dssl.trustAll=true"  # TODO: [Reverse Proxy] Simpler SSL checks for dev
if [ "${DEPLOYMENT_TYPE}" = "production" ]; then
  instance_type="production"
  ssl_trust_all=""
fi

echo "Starting ${instance_type} instance"

exec /opt/jdk/bin/java \
  -Xms"${JVM_MIN}" -Xmx"${JVM_MAX}" \
  -Dspring.profiles.active="${instance_type}" \
  -Dlogging.config=/var/backend/log4j2.xml \
  ${ssl_trust_all} \
  -jar /folding-stats.jar
