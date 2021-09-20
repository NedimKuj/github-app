package com.nedkuj.github.common

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class ArgumentBinder<T : Any> constructor(val key: String, val defaultValue: T) : Bind<T> {

    override operator fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.intent.extras?.get(key) as? T ?: defaultValue
    }


    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments?.get(key) as? T ?: defaultValue
    }
}