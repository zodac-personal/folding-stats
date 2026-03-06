/*
 * BSD Zero Clause License
 *
 * Copyright (c) 2021-2026 zodac.net
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package net.zodac.folding.rest.api.util;

import java.net.Socket;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import tools.jackson.core.json.JsonWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Simple utility class holding some constants.
 */
public final class RestUtilConstants {

    /**
     * Instance of {@link ObjectMapper} with:
     * <ul>
     *     <li>Pretty-printing enabled</li>
     *     <li>HTML escaping disabled</li>
     * </ul>
     */
    public static final ObjectMapper JSON_MAPPER = JsonMapper.builder()
        .enable(SerializationFeature.INDENT_OUTPUT) // Pretty print
        .disable(JsonWriteFeature.ESCAPE_NON_ASCII) // Disable HTML escaping
        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES) // Set null int values to 0
        .disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        .disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
        .build();

    /**
     * Instance of {@link HttpClient} with:
     * <ul>
     *     <li>HTTP protocol of {@link HttpClient.Version#HTTP_2}</li>
     *     <li>A connection timeout of <b>10</b> {@link java.util.concurrent.TimeUnit#SECONDS}</li>
     *     <li>A trust-all {@link SSLContext} with hostname verification disabled if the system property {@code ssl.trustAll} is {@code true}</li>
     * </ul>
     */
    public static final HttpClient HTTP_CLIENT = buildHttpClient();

    private RestUtilConstants() {

    }

    private static HttpClient buildHttpClient() {
        final HttpClient.Builder builder = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10L));

        // TODO: [Reverse Proxy] All this extra stuff to simplify SSL checks in dev
        if (Boolean.getBoolean("ssl.trustAll")) { // Only enabled during integration tests
            final TrustManager[] trustAllManagers = {new TrustAllX509TrustManager()};
            try {
                final SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllManagers, new SecureRandom());
                final SSLParameters sslParameters = new SSLParameters();
                sslParameters.setEndpointIdentificationAlgorithm(""); // Disable hostname verification
                builder.sslContext(sslContext).sslParameters(sslParameters);
            } catch (final KeyManagementException | NoSuchAlgorithmException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        return builder.build();
    }

    // Extends X509ExtendedTrustManager (not X509TrustManager) to prevent Java from wrapping it in
    // AbstractTrustManagerWrapper, which would add its own hostname verification on top of our no-op checks.
    private static final class TrustAllX509TrustManager extends X509ExtendedTrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            // Trust all clients
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType, final Socket socket) {
            // Trust all clients
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine) {
            // Trust all clients
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            // Trust all servers
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType, final Socket socket) {
            // Trust all servers
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine) {
            // Trust all servers
        }
    }
}
