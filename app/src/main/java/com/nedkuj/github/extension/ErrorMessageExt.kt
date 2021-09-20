package com.nedkuj.github.extension

import com.nedkuj.github.R.string
import com.nedkuj.github.model.base.ErrorMessageEnum
import com.nedkuj.github.model.base.ErrorMessageEnum.EMAIL_NOT_VERIFIED
import com.nedkuj.github.model.base.ErrorMessageEnum.EMAIL_REQUIRED
import com.nedkuj.github.model.base.ErrorMessageEnum.FORBIDDEN
import com.nedkuj.github.model.base.ErrorMessageEnum.INVALID_EMAIL_FORMAT
import com.nedkuj.github.model.base.ErrorMessageEnum.INVALID_PASSWORD_FORMAT
import com.nedkuj.github.model.base.ErrorMessageEnum.INVALID_PHONE_NUMBER
import com.nedkuj.github.model.base.ErrorMessageEnum.MISSING_PLATFORM_OR_VERSION
import com.nedkuj.github.model.base.ErrorMessageEnum.MISSING_TOS
import com.nedkuj.github.model.base.ErrorMessageEnum.NO_INTERNET
import com.nedkuj.github.model.base.ErrorMessageEnum.PAYLOAD_INVALID
import com.nedkuj.github.model.base.ErrorMessageEnum.SERVICE_TEMP_UNAVAILABLE
import com.nedkuj.github.model.base.ErrorMessageEnum.SOMETHING_WENT_WRONG
import com.nedkuj.github.model.base.ErrorMessageEnum.TC_NOT_ACCEPTED
import com.nedkuj.github.model.base.ErrorMessageEnum.TOKEN_EXPIRED
import com.nedkuj.github.model.base.ErrorMessageEnum.UNKNOWN
import com.nedkuj.github.model.base.ErrorMessageEnum.USERNAME_MISSING
import com.nedkuj.github.model.base.ErrorMessageEnum.USER_DOES_NOT_EXIST
import com.nedkuj.github.model.base.ErrorMessageEnum.USER_EXISTS
import com.nedkuj.github.model.base.ErrorMessageEnum.WRONG_PASSWORD
import com.nedkuj.github.model.base.ErrorMessageEnum.WRONG_RESPONSE
import com.nedkuj.github.model.base.ErrorMessageEnum.MISSING_REQUIRED_FIELDS

fun ErrorMessageEnum.getErrorMessageStringRes(): Int {
  return when (this) {
    SOMETHING_WENT_WRONG        -> string.something_went_wrong
    WRONG_PASSWORD              -> string.wrong_password
    USER_DOES_NOT_EXIST         -> string.user_does_not_exist
    INVALID_EMAIL_FORMAT        -> string.invalid_email_format
    INVALID_PASSWORD_FORMAT     -> string.invalid_email_format
    USER_EXISTS                 -> string.user_exists
    TC_NOT_ACCEPTED             -> string.terms_and_conditions_not_accepted
    INVALID_PHONE_NUMBER        -> string.invalid_phone_number
    SERVICE_TEMP_UNAVAILABLE    -> string.service_is_temporarily_unavailable
    TOKEN_EXPIRED               -> string.token_does_not_exist
    MISSING_PLATFORM_OR_VERSION -> string.missing_headers
    PAYLOAD_INVALID             -> string.invalid_payload
    NO_INTERNET                 -> string.no_internet_connection
    WRONG_RESPONSE              -> string.wrong_response
    UNKNOWN                     -> string.unknown_issue
    MISSING_TOS                 -> string.missing_tos
    EMAIL_REQUIRED              -> string.email_required
    FORBIDDEN                   -> string.forbidden
    EMAIL_NOT_VERIFIED          -> string.user_email_not_verified
    USERNAME_MISSING            -> string.username_missing
    MISSING_REQUIRED_FIELDS     -> string.fields_error
    else                        -> string.something_went_wrong
  }
} 
