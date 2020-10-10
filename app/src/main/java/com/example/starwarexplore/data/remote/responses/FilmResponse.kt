package com.example.starwarexplore.data.remote.responses

data class FilmResponse(
    val characters: List<String>,
    val created: String,
    val director: String,
    val edited: String,
    val episode_id: Int,
    val opening_crawl: String,
    val planets: List<String>,
    val producer: String,
    val release_date: String,
    val species: List<String>,
    val starships: List<String>,
    val title: String,
    val url: String,
    val vehicles: List<String>,
    var image:String?=null
){
    constructor(characters: List<String>,
                    created: String,
                    director: String,
                    edited: String,
                    episode_id: Int,
                    opening_crawl: String,
                    planets: List<String>,
                    producer: String,
                    release_date: String,
                    species: List<String>,
                    starships: List<String>,
                    title: String,
                    url: String,
                    vehicles: List<String>):this(

                            characters, created,
                            director,
                            edited,
                            episode_id,
                            opening_crawl,
                            planets,
                            producer,
                            release_date,
                            species ,
                            starships ,
                            title,
                            url,
                            vehicles ,
                            null
                    )
}