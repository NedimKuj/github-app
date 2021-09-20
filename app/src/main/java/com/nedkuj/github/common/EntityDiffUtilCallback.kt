package com.nedkuj.github.common

import androidx.recyclerview.widget.DiffUtil

class EntityDiffUtilCallback(
    private val oldList: List<Entity<*>>,
    private val newList: List<Entity<*>>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].id() == newList[newItemPosition].id()

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].equals(newList[newItemPosition])

}