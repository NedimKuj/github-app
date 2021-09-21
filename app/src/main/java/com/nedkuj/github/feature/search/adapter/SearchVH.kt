package com.nedkuj.github.feature.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.view.clicks
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseViewHolder
import com.nedkuj.github.databinding.FragmentSearchItemBinding
import com.nedkuj.github.model.Repository

class SearchVH(parent: ViewGroup, adapterView: SearchAdapterView) : BaseViewHolder<Repository, SearchAdapterView, FragmentSearchItemBinding>(
        DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_search_item,
                parent,
                false
        )
) {
    init {
        itemView.clicks()
                .map { data.name }
                .subscribe(adapterView.onItemPressed)

        binding.ownerImage.clicks()
                .map { data.owner.login }
                .subscribe(adapterView.onUserImagePressed)
    }
}