package com.hilguener.superheroesapp.model.comics

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Comic>,
    val total: Int
)