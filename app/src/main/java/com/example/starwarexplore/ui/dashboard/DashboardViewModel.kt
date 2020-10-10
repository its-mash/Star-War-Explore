package com.example.starwarexplore.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarexplore.data.remote.responses.EndpointResponse
import com.example.starwarexplore.repositories.IStarWarRepository
import com.example.starwarexplore.util.Event
import com.example.starwarexplore.util.Resource
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject constructor(
    private val repository: IStarWarRepository
) : ViewModel() {

    private val _endpoints = MutableLiveData<Event<Resource<EndpointResponse>>>()
    val endpoints: LiveData<Event<Resource<EndpointResponse>>> = _endpoints



    fun getEndPoints(){
        _endpoints.value=Event(Resource.loading(null));
        viewModelScope.launch {
            val response=repository.getEndPoints()
            _endpoints.value=Event(response)
        }
    }
}