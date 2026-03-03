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
import net.zodac.folding.api.tc.change.UserChange;
import net.zodac.folding.client.java.request.UserChangeRequestSender;
import net.zodac.folding.rest.api.tc.request.UserChangeRequest;
import net.zodac.folding.rest.api.util.RestUtilConstants;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.type.TypeReference;

/**
 * Utility class used to parse a {@link HttpResponse} returned from {@link UserChangeRequestSender}.
 */
public final class UserChangeResponseParser {

    private UserChangeResponseParser() {

    }

    /**
     * Returns the {@link UserChange}s retrieved by {@link UserChangeRequestSender#getAllWithoutPasskeys()},
     * {@link UserChangeRequestSender#getAllWithoutPasskeys(Collection)}, {@link UserChangeRequestSender#getAllWithPasskeys(String, String)} or
     * {@link UserChangeRequestSender#getAllWithPasskeys(Collection, String, String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the retrieved {@link UserChange}s
     */
    public static Collection<UserChange> getAll(final HttpResponse<String> response) {
        final String body = response.body();
        if (body == null || body.isBlank()) {
            return List.of();
        }
        return RestUtilConstants.JSON_MAPPER.readValue(body, new UserChangeCollectionType());
    }

    /**
     * Returns the {@link UserChange} retrieved by {@link UserChangeRequestSender#get(int, String, String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the retrieved {@link UserChange}, or {@code null} for a cached value (HTTP_304)
     */
    public static @Nullable UserChange get(final HttpResponse<String> response) {
        final String body = response.body();
        if (body == null || body.isBlank()) {
            return null;
        }
        return RestUtilConstants.JSON_MAPPER.readValue(body, UserChange.class);
    }

    /**
     * Returns the {@link UserChange} updated by {@link UserChangeRequestSender#reject(int, String, String)},
     * {@link UserChangeRequestSender#approveImmediately(int, String, String)} or
     * {@link UserChangeRequestSender#approveNextMonth(int, String, String)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the updated {@link UserChange}
     */
    public static UserChange update(final HttpResponse<String> response) {
        return RestUtilConstants.JSON_MAPPER.readValue(response.body(), UserChange.class);
    }

    /**
     * Returns the {@link UserChange} created by {@link UserChangeRequestSender#create(UserChangeRequest)}.
     *
     * @param response the {@link HttpResponse} to parse
     * @return the created {@link UserChange}
     */
    public static UserChange create(final HttpResponse<String> response) {
        return RestUtilConstants.JSON_MAPPER.readValue(response.body(), UserChange.class);
    }

    private static final class UserChangeCollectionType extends TypeReference<List<UserChange>> {

    }
}
