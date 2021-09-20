package com.nedkuj.github.exception

import com.nedkuj.github.model.base.ErrorMessageEnum

open class ApiErrorException(
    val errorMessage: ErrorMessageEnum,
    throwable: Throwable? = null
) : GenericException(throwable)
