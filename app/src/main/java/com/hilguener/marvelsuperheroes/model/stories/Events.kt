package com.hilguener.superheroesapp.model.stories

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)