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

import java.net.http.HttpClient;
import java.time.Duration;
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
     * </ul>
     */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10L))
        .build();

    private RestUtilConstants() {

    }
}
