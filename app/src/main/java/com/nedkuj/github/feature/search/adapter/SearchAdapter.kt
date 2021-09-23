package com.nedkuj.github.feature.search.adapter

import android.view.ViewGroup
import com.nedkuj.github.common.BaseAdapter
import com.nedkuj.github.model.Repository
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SearchAdapter @Inject constructor() : BaseAdapter<Repository, SearchVH>(), SearchAdapterView {

    override val onItemPressed: PublishSubject<Repository> = PublishSubject.create()
    override val onUserImagePressed: PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH = SearchVH(parent, this)
}