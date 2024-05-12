package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("entity") entity: String = "musicTrack"
    ): Response<SearchResponse>
}

data class SearchResponse(val results: List<Song>)

