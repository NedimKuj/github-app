package com.nedkuj.github.exception
import com.nedkuj.github.model.base.ErrorMessageEnum

class NoInternetException(
    throwable: Throwable? = null
) : ApiErrorException(ErrorMessageEnum.NO_INTERNET, throwable)
