#!/bin/sh
set -e

if [ "${NGINX_ENABLE_BACKEND_VHOST:-true}" != "true" ]; then
    echo "NGINX_ENABLE_BACKEND_VHOST is not 'true', removing backend vhost configs"
    rm -f /etc/nginx/conf.d/backend.conf
    rm -f /etc/nginx/conf.d/redirect-backend.conf
fi