package com.nedkuj.github.model.base


class FragmentResultEntity<T> {
    var requestCode: Int = 0
    var resultCode: Int = 0
    var resultData: T? = null

    companion object {
        const val OK = 0
        const val CANCEL = 1
    }
}
