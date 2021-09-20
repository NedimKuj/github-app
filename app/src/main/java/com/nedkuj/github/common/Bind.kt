package com.nedkuj.github.common

import android.app.Activity
import androidx.fragment.app.Fragment

interface Bind<T> {
    operator fun getValue(thisRef: Activity, property: kotlin.reflect.KProperty<*>): T
    operator fun getValue(thisRef: Fragment, property: kotlin.reflect.KProperty<*>): T

}
