package com.nedkuj.github.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseAdapter
import com.nedkuj.github.common.Entity
import com.nedkuj.github.extension.loadFromUrl

@BindingAdapter("data")
fun <T : Entity<*>> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>?) {
    if (recyclerView.adapter is BaseAdapter<*, *> && items != null) {
        (recyclerView.adapter as BaseAdapter<T, *>).items = items
    }
}

@BindingAdapter("android:text")
fun setText(textView: TextView, int: Int) {
    textView.text = int.toString()
}

@BindingAdapter("android:srcNetwork")
fun loadFromUrl(view: ImageView, url: String) {
    view.loadFromUrl(url, null, R.color.colorTextPrimary)
}