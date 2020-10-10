package com.example.starwarexplore.repositories

import android.R
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.starwarexplore.data.remote.StarWarAPI
import com.example.starwarexplore.data.remote.responses.CharactersWithImageLinkResponseItem
import com.example.starwarexplore.data.remote.responses.EndpointResponse
import com.example.starwarexplore.data.remote.responses.FilmResponse
import com.example.starwarexplore.data.remote.responses.PeopleResponse
import com.example.starwarexplore.util.Constants.START_WAR_CHARACTER_IMAGE_API
import com.example.starwarexplore.util.Constants.STAR_WAR_COVER_IMAGE_API
import com.example.starwarexplore.util.Resource
import com.example.starwarexplore.util.Status
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class StarWarRepository @Inject constructor(private val starWarAPI: StarWarAPI) : IStarWarRepository {
    override suspend fun getEndPoints(): Resource<EndpointResponse> {
        return try {
            val response = starWarAPI.getEndPoints()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it, null)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }


    override suspend fun observeFilms(): Resource<List<FilmResponse>> {
        val endpoints = getEndPoints();
        if (endpoints.status == Status.ERROR)
            return Resource.error(endpoints.message!!, null);
        val filmEndpoint = endpoints.data!!.films

        return try {
            val response = starWarAPI.getFilms(filmEndpoint)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.results.forEach { it1 -> it1.image = getMovieImage(it1.episode_id) }
                    return@let Resource.success(it.results, null)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getFilm(url: String): Resource<FilmResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun observePeoples(): Resource<List<PeopleResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPeople(url: String): Resource<PeopleResponse> {
        return try {
            val response = starWarAPI.getCharacter(url)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it, null)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getFilmPeoples(film: FilmResponse): List<Resource<PeopleResponse>> {
        val characters = mutableListOf<Resource<PeopleResponse>>()
        for (characterUrl in film.characters) {
            characters.add(getPeople(characterUrl))
        }

        val charactersWithImageLinkResponse = runBlocking { getCharactersWithImage() }
//        if(charactersWithImageLinkResponse.status==Status.SUCCESS){
        characters.forEach {
//               if(it.status==Status.SUCCESS){
            charactersWithImageLinkResponse.data?.forEach { it1 ->
                if (it1.name == it.data?.name) {
                    val data = it.data
                    if (data != null) data.image = it1.image
                }
            }
//               }
        }
//        }
        print(charactersWithImageLinkResponse)
        return characters;
    }

    override fun getMovieImage(episodeNumber: Int): String {
        return "$STAR_WAR_COVER_IMAGE_API$episodeNumber.jpg"
    }

    override suspend fun getCharactersWithImage(): Resource<List<CharactersWithImageLinkResponseItem>> {
        val call: Call<List<CharactersWithImageLinkResponseItem?>?>? = starWarAPI.getCharactersWithImageLink(
            START_WAR_CHARACTER_IMAGE_API
        )
        lateinit var res:Resource<List<CharactersWithImageLinkResponseItem>>
        call!!.enqueue(object : Callback<List<CharactersWithImageLinkResponseItem?>?> {
            override fun onResponse(call: Call<List<CharactersWithImageLinkResponseItem?>?>?, response: Response<List<CharactersWithImageLinkResponseItem?>?>) {
                Log.d("ABC SUCCESS",response.body().toString())
                res= Resource.success(response.body(),null) as Resource<List<CharactersWithImageLinkResponseItem>>
            }

            override fun onFailure(call: Call<List<CharactersWithImageLinkResponseItem?>?>?, t: Throwable) {
                Log.d("ABC ERROR",t.toString())
                res=Resource.error(t.toString(),null)
            }
        })
        return Resource.success(null,null)
//        try {
//            val response = starWarAPI.getCharactersWithImageLink(START_WAR_CHARACTER_IMAGE_API)
//            if(response.isSuccessful) {
//                response.body()?.let {
//                    Log.d("RRR",response.body().toString())
//                    return@let Resource.success(it,null)
//                } ?: Resource.error("An unknown error occured", null)
//            } else {
//                Resource.error("An unknown error occured", null)
//            }
//        } catch(e: Exception) {
//            Resource.error(e.localizedMessage, null)
//        }
//        return Resource.success(null,null)
    }
}
