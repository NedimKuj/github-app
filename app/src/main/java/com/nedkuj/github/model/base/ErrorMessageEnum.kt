package com.nedkuj.github.model.base

import com.squareup.moshi.Json

enum class ErrorMessageEnum {
    SOMETHING_WENT_WRONG,

    @Json(name = "wrong_password")
    WRONG_PASSWORD,

    @Json(name = "user_does_not_exist")
    USER_DOES_NOT_EXIST,

    @Json(name = "invalid_email_format")
    INVALID_EMAIL_FORMAT,

    @Json(name = "invalid_password_format")
    INVALID_PASSWORD_FORMAT,

    @Json(name = "user_exists")
    USER_EXISTS,

    @Json(name = "terms_and_conditions_not_accepted")
    TC_NOT_ACCEPTED,

    @Json(name = "invalid_phone_number")
    INVALID_PHONE_NUMBER,

    @Json(name = "service_is_temporarily_unavailable")
    SERVICE_TEMP_UNAVAILABLE,

    @Json(name = "token_does_not_exist")
    TOKEN_EXPIRED,

    @Json(name = "app-platform_or_app-version_is_missing_in_request_headers")
    MISSING_PLATFORM_OR_VERSION,

    @Json(name = "request_payload_validation")
    PAYLOAD_INVALID,
    MISSING_TOS,
    NO_INTERNET,
    USERNAME_MISSING,

    @Json(name = "email_is_required")
    EMAIL_REQUIRED,
    WRONG_RESPONSE,

    @Json(name = "forbidden")
    FORBIDDEN,

    @Json(name = "user_email_not_verified")
    EMAIL_NOT_VERIFIED,
    UNKNOWN,
    PASSWORD_ERROR,
    EMAIL_ERROR,
    NAME_ERROR,
    LAST_NAME_ERROR,
    INVALID_USERNAME,
    MISSING_REQUIRED_FIELDS
}
