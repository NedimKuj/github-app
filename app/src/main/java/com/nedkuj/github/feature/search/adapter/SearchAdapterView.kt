package com.nedkuj.github.feature.search.adapter

import io.reactivex.subjects.PublishSubject

interface SearchAdapterView {
    val onItemPressed: PublishSubject<String>
    val onUserImagePressed: PublishSubject<String>
}