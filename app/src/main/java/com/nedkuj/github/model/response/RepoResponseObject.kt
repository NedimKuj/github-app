package com.nedkuj.github.model.response

import android.os.Parcelable
import com.nedkuj.github.model.Repository
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoResponseObject(
        val items: List<Repository>
) : Parcelable
