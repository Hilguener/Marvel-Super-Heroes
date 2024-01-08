package com.hilguener.superheroesapp.model.character

import com.hilguener.superheroesapp.model.comics.Thumbnail

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int,
    val thumbnail: Thumbnail
)