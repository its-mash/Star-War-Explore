package com.example.starwarexplore.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarexplore.data.remote.responses.CharactersWithImageLinkResponseItem
import com.example.starwarexplore.data.remote.responses.EndpointResponse
import com.example.starwarexplore.data.remote.responses.FilmResponse
import com.example.starwarexplore.data.remote.responses.PeopleResponse
import com.example.starwarexplore.repositories.IStarWarRepository
import com.example.starwarexplore.util.Event
import com.example.starwarexplore.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DashboardViewModel @ViewModelInject constructor(
    private val repository: IStarWarRepository
) : ViewModel() {

    private val _endpoints = MutableLiveData<Event<Resource<EndpointResponse>>>()
    val endpoints: LiveData<Event<Resource<EndpointResponse>>> = _endpoints


    private val _films= MutableLiveData<Event<Resource< List<FilmResponse> >>>()
    val films:LiveData<Event<Resource<List<FilmResponse>>>> =_films

    private val _characters= MutableLiveData<Event<Resource< List<PeopleResponse> >>>()
    val characters:LiveData<Event<Resource<List<PeopleResponse>>>> =_characters

//    private val _filmCharacters= MutableLiveData<Event<Resource<List<Resource<PeopleResponse>>>>>()
//    val filmCharacters:LiveData<Event<Resource<List<Resource<PeopleResponse>>>>> =_filmCharacters

    fun getEndPoints(){
        _endpoints.value=Event(Resource.loading(null));
        viewModelScope.launch {
            val response=repository.getEndPoints()
            _endpoints.value=Event(response)
        }
    }

    fun getFilms(){
        _films.value= Event(Resource.loading(null))
        viewModelScope.launch {
            val response=repository.observeFilms()
            _films.value=Event(response)
        }
    }

    fun getCharacters(){
        _characters.value= Event(Resource.loading(null))
        viewModelScope.launch {
            val response=repository.observePeoples()
            _characters.value=Event(response)
        }
    }

    fun getFilmCharacters(film:FilmResponse):LiveData<Event<Resource<List<Resource<PeopleResponse>>>>>{
        val _filmCharacters= MutableLiveData<Event<Resource<List<Resource<PeopleResponse>>>>>()
        val filmCharacters:LiveData<Event<Resource<List<Resource<PeopleResponse>>>>> =_filmCharacters

        _filmCharacters.value=Event(Resource.loading(null))
        viewModelScope.launch {
            val characters= mutableListOf<Resource<PeopleResponse>>()
            for(character in film.characters) {
                characters.add(repository.getPeople(character))
            }
            _filmCharacters.value=Event(Resource.success(characters,null))
        }
        return filmCharacters
    }
    fun getCharacter(url:String):Resource<PeopleResponse>{
       return runBlocking {  repository.getPeople(url) }
    }

    fun getCharactersWithImage():LiveData< Event<Resource<List<CharactersWithImageLinkResponseItem > > > >{
        val _chars= MutableLiveData< Event<Resource<List<CharactersWithImageLinkResponseItem>>>>()
        val chars:LiveData< Event<Resource<List<CharactersWithImageLinkResponseItem>>>> =_chars
        _chars.value=Event(Resource.loading(null))
        viewModelScope.launch {
            _chars.value=Event(repository.getCharactersWithImage())
        }
        return chars;
    }

}