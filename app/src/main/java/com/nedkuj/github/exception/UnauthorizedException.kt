package com.nedkuj.github.exception

class UnauthorizedException(throwable: Throwable, val code: Int) : GenericException(throwable)
