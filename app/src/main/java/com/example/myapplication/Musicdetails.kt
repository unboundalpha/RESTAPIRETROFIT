package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Musicdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicdetails)
        val trackId = intent.getLongExtra("trackId", 0)
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val previewUrl = intent.getStringExtra("previewUrl")
        val TrackidTextview=findViewById<TextView>(R.id.trackIdTextView)
        val TracknameTextview=findViewById<TextView>(R.id.trackNameTextView)
        val artistNameTextView=findViewById<TextView>(R.id.artistNameTextView)
        val previewUrlTextview=findViewById<TextView>(R.id.previewUrlTextView)
        TrackidTextview.text= trackId.toString()

        TracknameTextview.text=trackName.toString()
        artistNameTextView.text=artistName.toString()
        previewUrlTextview.text=previewUrl.toString()
    }
    override fun onBackPressed() {
        // Customize what happens when the back button is pressed
        // For example, you can close the activity or perform other actions
        finish()
        super.onBackPressed()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//    }
}