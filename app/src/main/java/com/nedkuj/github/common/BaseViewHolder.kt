package com.nedkuj.github.common

import androidx.annotation.CallSuper
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : Any, AV : Any, VB : ViewDataBinding>(
    val binding: VB,
    protected val adapterView: AV? = null
) : RecyclerView.ViewHolder(binding.root) {

    val bindingData = BindingData()
    lateinit var data: T

    @CallSuper
    open fun bind(data: T) {
        this.data = data
//        binding.setVariable(BR.item, this)
        bindingData.data.set(data)
    }

    inner class BindingData {
        val data: ObservableField<T> = ObservableField()
    }
}