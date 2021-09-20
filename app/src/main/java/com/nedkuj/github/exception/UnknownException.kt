package com.nedkuj.github.exception
import com.nedkuj.github.model.base.ErrorMessageEnum

class UnknownException(
    throwable: Throwable
) : ApiErrorException(ErrorMessageEnum.UNKNOWN, throwable)
