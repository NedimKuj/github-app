package com.nedkuj.github.exception

import com.nedkuj.github.model.base.ErrorMessageEnum


open class GenericDataException(
    val error: ErrorMessageEnum,
    throwable: Throwable? = null
) : GenericException(throwable)