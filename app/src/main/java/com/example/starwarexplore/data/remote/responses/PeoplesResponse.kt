package com.example.starwarexplore.data.remote.responses

data class PeoplesResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PeopleResponse>
)