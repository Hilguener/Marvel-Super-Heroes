package com.hilguener.superheroesapp.model.comics

data class ComicsDTO<T>(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val `data`: Data,
    val etag: String,
    val status: String
)