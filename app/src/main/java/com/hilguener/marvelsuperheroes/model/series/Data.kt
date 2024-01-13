package com.hilguener.marvelsuperheroes.model.series

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Serie>,
    val total: Int
)