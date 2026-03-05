FROM node:25.7.0-alpine AS raml
# Install raml2html
RUN npm install --ignore-scripts -g raml2html@7.8.0

ARG REST_ENDPOINT_URL
ARG TITLE

RUN mkdir /docker/
WORKDIR /docker/

COPY ./docker/frontend/raml/ /docker/nginx/raml/

# Replace environment arguments in RAML files, then build RAML output HTML
SHELL ["/bin/ash", "-o", "pipefail", "-c"]
RUN grep -rl '%REST_ENDPOINT_URL%' . | xargs sed -i "s|%REST_ENDPOINT_URL%|${REST_ENDPOINT_URL}|g" && \
    grep -rl '%TITLE%' . | xargs sed -i "s|%TITLE%|${TITLE}|g" && \
    raml2html --input /docker/nginx/raml/api.raml --output /docker/nginx/api.html

FROM tdewolff/minify:v2.24.10 AS minifier

ARG REST_ENDPOINT_URL
ARG UPDATE_ENABLED

RUN mkdir /docker/
WORKDIR /docker/

# Copy the JS and CSS files to run the minification
COPY ./docker/frontend/css /docker/nginx/css
COPY ./docker/frontend/js /docker/nginx/js

# Replace environment arguments in JS files
SHELL ["/bin/ash", "-o", "pipefail", "-c"]
RUN grep -rl '%REST_ENDPOINT_URL%' . | xargs sed -i "s|%REST_ENDPOINT_URL%|${REST_ENDPOINT_URL}|g" && \
    grep -rl '%UPDATE_ENABLED%' . | xargs sed -i "s|%UPDATE_ENABLED%|${UPDATE_ENABLED}|g"

# Minify JS and CSS files, then rename to end with *.min.*
WORKDIR /docker/nginx/
RUN minify --recursive --verbose --type=css --output /docker/nginx/site/res/css/ css/ && \
    minify --recursive --verbose --type=js --output /docker/nginx/site/res/js/ js/ && \
    mv /docker/nginx/site/res/js/util/*.js /docker/nginx/site/res/js/ && \
    rm -rf /docker/nginx/site/res/js/util/ && \
    find /docker/nginx/site/res/ -iname "*.css" | sed 's|....$||' | xargs -I {} mv {}.css {}.min.css && \
    find /docker/nginx/site/res/ -iname "*.js" | sed 's|...$||' | xargs -I {} mv {}.js {}.min.js

FROM nginx:1.29.5-alpine

# Copy nginx config
COPY ./docker/frontend/conf/nginx.conf /etc/nginx/nginx.conf
COPY ./docker/frontend/conf/*.template /etc/nginx/templates/
RUN mv /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh.disabled

# Startup script: conditionally remove backend vhost configs (runs after envsubst)
COPY ./docker/frontend/scripts/configure-vhosts.sh /docker-entrypoint.d/21-configure-vhosts.sh
RUN chmod +x /docker-entrypoint.d/21-configure-vhosts.sh

WORKDIR /usr/share/nginx/html

# Website structure
COPY ./docker/frontend/pages/robots.txt \
     ./docker/frontend/pages/index.html \
     ./

# The index.html file requires the file extension since that is what the axihub URL points to
# The other files are all referenced relatively, so can get away without the extension to clean up the displayed URL
COPY ./docker/frontend/pages/results.html ./results
COPY ./docker/frontend/pages/admin.html ./admin
COPY ./docker/frontend/pages/requests.html ./requests
COPY ./docker/frontend/pages/historic/historic_team_daily.html ./historic_team_daily
COPY ./docker/frontend/pages/historic/historic_team_hourly.html ./historic_team_hourly
COPY ./docker/frontend/pages/historic/historic_team_monthly.html ./historic_team_monthly
COPY ./docker/frontend/pages/historic/historic_user_daily.html ./historic_user_daily
COPY ./docker/frontend/pages/historic/historic_user_hourly.html ./historic_user_hourly
COPY ./docker/frontend/pages/historic/historic_user_monthly.html ./historic_user_monthly

# Copy the RAML API and minified JS/CSS files
COPY --from=raml /docker/nginx/api.html ./api.html
COPY --from=minifier /docker/nginx/site/res ./res

# Add a hash to each of the JS files to avoid caching them after changes are made
# Also update all existing HTML pages references to the files to the new name
COPY ./docker/frontend/scripts/hash_files.sh /hash_files.sh
RUN chmod +x /hash_files.sh && \
    sh /hash_files.sh "js" && \
    sh /hash_files.sh "css" && \
    rm -f /hash_files.sh

# Copy the static content (images, 3PP JS/CSS files) only after the src JS files have been renamed and moved
COPY ./docker/frontend/res/img ./res/img
COPY ./docker/frontend/res/js/*.js ./res/js/
COPY ./docker/frontend/res/css/*.css ./res/css/

ARG FORUM_LINK
ARG ROOT_URL
ARG TITLE

# Replace environment arguments in HTML files
SHELL ["/bin/ash", "-o", "pipefail", "-c"]
RUN grep -rl '%FORUM_LINK%' . | xargs sed -i "s|%FORUM_LINK%|${FORUM_LINK}|g" && \
    grep -rl '%ROOT_URL%' . | xargs sed -i "s|%ROOT_URL%|${ROOT_URL}|g" && \
    grep -rl '%TITLE%' . | xargs sed -i "s|%TITLE%|${TITLE}|g"

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --start-period=10s \
            --retries=3 \
            CMD wget --no-check-certificate --quiet --tries=1 --spider https://127.0.0.1:443/ -O /dev/null --header="Host: ${NGINX_FRONTEND_URL}"
