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

package net.zodac.folding.rest.api;

import io.swagger.v3.oas.annotations.media.Schema;
import net.zodac.folding.api.util.EncodingUtils;

/**
 * Simple POJO defining {@link java.util.Base64}-encoded credentials. The credentials are created by:
 * <ol>
 *     <li>Concatenate the username and password, with a {@code :} as delimiter (i.e., 'username:password')</li>
 *     <li>Encode the result with {@link java.util.Base64}</li>
 *     <li>Prefix the encoded result with "Basic " (note the space)</li>
 * </ol>
 *
 * @param encodedUserNameAndPassword the {@link java.util.Base64}-encoded username/password
 */
@Schema(name = "LoginCredentials",
    description = "The JSON representation of the login credentials for the /admin endpoint",
    example = """
        {
            "encodedUserNameAndPassword": "Basic dXNlcm5hbWU6cGFzc3dvcmQ="
        }"""
)
public record LoginCredentials(
    @Schema(
        description = "The Base64-encoded username/password",
        example = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_ONLY
    )
    String encodedUserNameAndPassword
) {

    /**
     * Creates an instance of {@link LoginCredentials} given a {@link java.util.Base64}-encoded username/password credentials.
     *
     * <p>
     * If the input does not start with {@link EncodingUtils#BASIC_AUTHENTICATION_SCHEME}, it will be prefixed.
     *
     * @param encodedUserNameAndPassword the {@link java.util.Base64}-encoded username/password
     * @return the {@link LoginCredentials} for the given credentials
     */
    public static LoginCredentials createWithBasicAuthentication(final String encodedUserNameAndPassword) {
        if (encodedUserNameAndPassword.startsWith(EncodingUtils.BASIC_AUTHENTICATION_SCHEME)) {
            return new LoginCredentials(encodedUserNameAndPassword);
        }

        return new LoginCredentials(EncodingUtils.BASIC_AUTHENTICATION_SCHEME + encodedUserNameAndPassword);
    }
}
