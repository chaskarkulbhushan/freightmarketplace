package com.logix.freightmarketplace.common.response;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        ApiErrorCode errorCode,
        String message,
        String path
) {
}
