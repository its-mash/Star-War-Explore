package com.example.starwarexplore.ui.home

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.starwarexplore.R
import com.example.starwarexplore.ui.dashboard.DashboardViewModel
import com.example.starwarexplore.util.Status

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        textView.movementMethod= ScrollingMovementMethod()


        dashboardViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)

//        dashboardViewModel.endpoints.observe(viewLifecycleOwner, Observer {
//            textView.text = it.getContentIfNotHandled()?.data.toString()
//        })
        dashboardViewModel.films.observe(viewLifecycleOwner, Observer {
//            textView.text=it.getContentIfNotHandled()?.toString()
            val filmsRespone=it.getContentIfNotHandled()

            if(filmsRespone?.status== Status.SUCCESS) {
//                val film = filmsRespone!!.data!!.first()
                textView.text=filmsRespone.toString()
            }
            else if(filmsRespone?.status== Status.LOADING){
                textView.text="Film "+filmsRespone.status.toString()
            }
            else if(filmsRespone?.status== Status.ERROR)
                textView.text="Film "+filmsRespone.message
        })
//        dashboardViewModel.getEndPoints()
        dashboardViewModel.getFilms()
//        dashboardViewModel.getCharactersWithImage().observe(viewLifecycleOwner, Observer {
//            textView.text=it.getContentIfNotHandled().toString()
//        })

        return root
    }
}