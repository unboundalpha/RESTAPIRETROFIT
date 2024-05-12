package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class Song(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val previewUrl: String
)
