package com.nedkuj.github.common

interface BaseView<FVS> {
    fun render(viewState: FVS)
    fun setResult(result: Any)
}