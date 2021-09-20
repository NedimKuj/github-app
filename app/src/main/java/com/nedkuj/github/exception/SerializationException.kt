package com.nedkuj.github.exception
import com.nedkuj.github.model.base.ErrorMessageEnum

class SerializationException(
    throwable: Throwable? = null
) : ApiErrorException(ErrorMessageEnum.WRONG_RESPONSE, throwable)
