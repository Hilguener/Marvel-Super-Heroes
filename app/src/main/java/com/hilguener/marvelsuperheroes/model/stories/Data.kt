package com.hilguener.superheroesapp.model.stories

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Story>,
    val total: Int
)