package com.nedkuj.github.model

import android.os.Parcelable
import com.nedkuj.github.common.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repository(
        val id: Long,
        val name: String,
        val full_name: String,
        val owner: Owner,
        val url: String,
        val forks: Int,
        val watchers: Int,
        val open_issues: Int
) : Parcelable, Entity<Long> {
    override fun id(): Long = id
}