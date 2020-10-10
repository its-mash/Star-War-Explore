package com.example.starwarexplore.ui.dashboard

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.starwarexplore.R
import com.example.starwarexplore.util.Status
import kotlinx.serialization.json.Json

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        textView.movementMethod=ScrollingMovementMethod()

//        dashboardViewModel.endpoints.observe(viewLifecycleOwner, Observer {
//            textView.text = it.getContentIfNotHandled()?.data.toString()
//        })
        dashboardViewModel.films.observe(viewLifecycleOwner, Observer {
//            textView.text=it.getContentIfNotHandled()?.toString()
            val filmRespone=it.getContentIfNotHandled()

            if(filmRespone?.status==Status.SUCCESS) {
                val film = filmRespone!!.data!!.first()
                textView.text=film.toString()
                dashboardViewModel.getFilmCharacters(film).observe(viewLifecycleOwner, Observer {
                    val response = it.getContentIfNotHandled()
                    if (response?.status == Status.SUCCESS)
                        textView.text = response?.data.toString()
                    else if (response?.status == Status.LOADING)
                        textView.text = "Char " +Status.LOADING.toString()
                    else if (response?.status == Status.ERROR)
                        textView.text = "Char " +response.message
                })
            }
            else if(filmRespone?.status==Status.LOADING){
                textView.text="Film "+filmRespone.status.toString()
            }
            else if(filmRespone?.status==Status.ERROR)
                textView.text="Film "+filmRespone.message
        })
//        dashboardViewModel.getEndPoints()
        dashboardViewModel.getFilms()
        return root
    }
}