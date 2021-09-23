package com.nedkuj.github.feature.search.adapter

import com.nedkuj.github.model.Repository
import io.reactivex.subjects.PublishSubject

interface SearchAdapterView {
    val onItemPressed: PublishSubject<Repository>
    val onUserImagePressed: PublishSubject<String>
}