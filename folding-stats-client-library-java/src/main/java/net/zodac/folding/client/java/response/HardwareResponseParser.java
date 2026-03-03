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

package net.zodac.folding.client.java.response;

import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;
import net.zodac.folding.api.tc.Hardware;
import net.zodac.folding.client.java.request.HardwareRequestSender;
import net.zodac.folding.rest.api.tc.request.HardwareRequest;
import net.zodac.folding.rest.api.util.RestUtilConstants;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.type.TypeReference;

/**
 * Utility class used to parse a {@link HttpResponse} returned from {@link HardwareRequestSender}.
 */
public final class HardwareResponseParser {

    private HardwareResponseParser() {

    }

    /**
     * Returns the {@link Hardware}s retrieved by {@link HardwareRequestSender#getAll()}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the retrieved {@link Hardware}s
     */
    public static Collection<Hardware> getAll(final HttpResponse<String> response) {
        final String body = response.body();
        if (body == null || body.isBlank()) {
            return List.of();
        }

        return RestUtilConstants.JSON_MAPPER.readValue(body, new HardwareCollectionType());
    }

    /**
     * Returns the {@link Hardware} retrieved by {@link HardwareRequestSender#get(int)} or
     * {@link HardwareRequestSender#get(String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the retrieved {@link Hardware}, or {@code null} for a cached value (HTTP_304)
     */
    public static @Nullable Hardware get(final HttpResponse<String> response) {
        final String body = response.body();
        if (body == null || body.isBlank()) {
            return null;
        }

        return RestUtilConstants.JSON_MAPPER.readValue(body, Hardware.class);
    }

    /**
     * Returns the {@link Hardware} created by
     * {@link HardwareRequestSender#create(HardwareRequest, String, String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the created {@link Hardware}
     */
    public static Hardware create(final HttpResponse<String> response) {
        return RestUtilConstants.JSON_MAPPER.readValue(response.body(), Hardware.class);
    }

    /**
     * Returns the {@link Hardware} updated by
     * {@link HardwareRequestSender#update(int, HardwareRequest, String, String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the updated {@link Hardware}
     */
    public static Hardware update(final HttpResponse<String> response) {
        return RestUtilConstants.JSON_MAPPER.readValue(response.body(), Hardware.class);
    }

    private static final class HardwareCollectionType extends TypeReference<List<Hardware>> {

    }
}
