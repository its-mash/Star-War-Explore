package com.example.starwarexplore.repositories

import com.example.starwarexplore.data.remote.StarWarAPI
import com.example.starwarexplore.data.remote.responses.*
import com.example.starwarexplore.util.Resource
import com.example.starwarexplore.util.Status
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StarWarRepository @Inject constructor(private val starWarAPI:StarWarAPI) : IStarWarRepository{
    override suspend fun getEndPoints(): Resource<EndpointResponse> {
        return try {
            val response =  starWarAPI.getEndPoints()
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it,null)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch(e: Exception) {
             Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getFilms(): Resource< List< FilmResponse> > {
        val endpoints=getEndPoints();
        if(endpoints.status== Status.ERROR)
            return Resource.error(endpoints.message!!,null);
        val filmEndpoint=endpoints.data!!.films

        return try {
            val response = starWarAPI.getFilms(filmEndpoint)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it.results,null)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getFilm(url: String): Resource<FilmResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPeoples(): Resource<PeoplesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPeople(url: String): Resource<PeopleResponse> {
        TODO("Not yet implemented")
    }

    override fun getMovieImage(episodeNumber: Int): String {
        TODO("Not yet implemented")
    }

    override fun getCharacterImage(name: String): String {
        TODO("Not yet implemented")
    }
}