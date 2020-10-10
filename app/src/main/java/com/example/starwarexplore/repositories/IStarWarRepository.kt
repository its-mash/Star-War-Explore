package com.example.starwarexplore.repositories

import com.example.starwarexplore.data.remote.responses.*
import com.example.starwarexplore.util.Resource

interface IStarWarRepository {

    suspend fun getEndPoints(): Resource<EndpointResponse>

    suspend fun getFilms(): Resource<List<FilmResponse>>

    suspend fun getFilm(url:String): Resource<FilmResponse>

    suspend fun getPeoples(): Resource<PeoplesResponse>

    suspend fun getPeople(url: String): Resource<PeopleResponse>

    fun getMovieImage(episodeNumber: Int): String
    fun getCharacterImage(name: String): String
}