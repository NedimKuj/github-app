package com.nedkuj.github.model

enum class SortState(val value: String) {
    SORT_DEFAULT_BEST_MATCH("best-match"),
    SORT_STARS("stars"),
    SORT_FORKS("forks"),
    SORT_UPDATED("updated");

    companion object {
        fun from(string: String): SortState {
            return values().first { it.value == string }
        }
    }
}