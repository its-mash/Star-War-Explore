package com.example.starwarexplore.data.remote

import com.example.starwarexplore.data.remote.responses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface StarWarAPI {
    @GET("/api")
    suspend fun getEndPoints(): Response<EndpointResponse>

    @GET
    suspend fun getFilms(@Url url: String): Response<FilmsResponse>

    @GET
    suspend fun getFilm(@Url url: String): Response<FilmResponse>

    @GET
    suspend fun getCharacters(@Url url: String): Response<PeoplesResponse>

    @GET
    suspend fun getCharacter(@Url url: String): Response<PeopleResponse>
}