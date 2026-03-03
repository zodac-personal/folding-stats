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

package net.zodac.folding.rest.api.tc.request;

import io.swagger.v3.oas.annotations.media.Schema;
import net.zodac.folding.api.RequestPojo;
import net.zodac.folding.api.tc.stats.OffsetTcStats;

/**
 * REST request to create/update a {@link OffsetTcStats}.
 *
 * @param pointsOffset           the points offset
 * @param multipliedPointsOffset the multiplied points offset
 * @param unitsOffset            the units offset
 */
@Schema(name = "OffsetTcStatsRequest",
    description = "A JSON request to offset the stats for a user through the /stats endpoint " +
        "(at least one of 'pointsOffset' or 'multipliedPointsOffset' must be non-zero)",
    example = """
        {
          "pointsOffset": 150,
          "multipliedPointsOffset": 1500,
          "unitsOffset": 15
        }"""
)
// TODO: Replace with OffsetTcStats
public record OffsetTcStatsRequest(
    @Schema(
        description = "The points offset (positive of negative) to be applied to a user's TC stats",
        example = "150",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    long pointsOffset,
    @Schema(
        description = "The multiplied points offset (positive of negative) to be applied to a user's TC stats",
        example = "1500",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    long multipliedPointsOffset,
    @Schema(
        description = "The units offset (positive of negative) to be applied to a user's TC stats",
        example = "15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    int unitsOffset
) implements RequestPojo {

}
