package com.michaelgunawan.cryptolist.model

enum class Sorting(val identifier: String) {
    ASCENDING("asc"), DESCENDING("desc")
}

fun Sorting?.reverse(): Sorting {
    return if (this == Sorting.ASCENDING) {
        Sorting.DESCENDING
    } else {
        Sorting.ASCENDING
    }
}