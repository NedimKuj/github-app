package com.nedkuj.github.extension

import com.nedkuj.github.exception.ApiErrorException
import com.nedkuj.github.model.base.ErrorMessageEnum

fun Throwable.toErrorMessage(): ErrorMessageEnum {
  return if (this is ApiErrorException) {
    this.errorMessage
  } else {
    ErrorMessageEnum.SOMETHING_WENT_WRONG
  }
}
