package com.hilguener.superheroesapp.model.character


data class Results(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Character>
)