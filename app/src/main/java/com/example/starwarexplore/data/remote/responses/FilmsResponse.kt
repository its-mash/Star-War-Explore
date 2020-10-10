package com.example.starwarexplore.data.remote.responses

data class FilmsResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<FilmResponse>
)