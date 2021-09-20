package com.nedkuj.github.extension


import android.text.TextUtils
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isNotValidEmail() = ! isValidEmail()

fun String.toDate(): Date? {
    if(this.isEmpty()) return null
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return format.parse(this)
}