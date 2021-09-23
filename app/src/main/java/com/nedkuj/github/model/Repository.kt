package com.nedkuj.github.model

import android.os.Parcelable
import com.nedkuj.github.common.Entity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val private: Boolean,
    val owner: Owner,
    val description: String,
    val url: String,
    val language: String,
    val created_at: Date,
    val updated_at: Date,
    val forks: Int,
    val watchers: Int,
    val open_issues: Int
) : Parcelable, Entity<Long> {
    override fun id(): Long = id
}