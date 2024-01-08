package com.hilguener.superheroesapp.model.stories

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)