package com.nedkuj.github.model.base

data class Error(
    val code: Int,
    val cause: String,
    val message: ErrorMessageEnum
)
