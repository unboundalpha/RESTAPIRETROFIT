package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SongAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SongAdapter
    private lateinit var ShowFav: Button
    private lateinit var favoritesSet:MutableSet<String>
    private lateinit var searchEditText:EditText
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/") // Ensure the base URL ends with a forward slash
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("MyFavorites", MODE_PRIVATE)
        favoritesSet = sharedPreferences.getStringSet("favoritesSet", HashSet())?.toMutableSet() ?: HashSet()
        ShowFav=findViewById(R.id.displayfav)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SongAdapter(ArrayList(), this,favoritesSet)
        recyclerView.adapter = adapter
        searchEditText = findViewById<EditText>(R.id.searchEditText)
        val LogoutButton=findViewById<Button>(R.id.Logout)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation neededbe
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString().trim()
                fetchSongs(searchTerm)
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })
        fetchSongs("")
        ShowFav.setOnClickListener {
            fetchSongs("")
            searchEditText.visibility= View.GONE
            fetchFavorates(favoritesSet)
            ShowFav.visibility=View.GONE
        }
        LogoutButton.setOnClickListener {
            clearCredentials(this)
            val Lintent = Intent(this, Loginregister::class.java)
            startActivity(Lintent)
            finish()

        }
    }
    fun clearCredentials(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun fetchFavorates(tt: MutableSet<String>){

        for (trackName in tt) {
            // Fetch each favorite song by its track name
            Log.d("XYZABC","TravkName"+trackName)
            fetchSongsFAV(trackName)
        }
    }

    private fun fetchSongsFAV(trackName: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.searchSongs(trackName)
                if (response.isSuccessful) {
                    val songs = response.body()?.results ?: emptyList()
                    adapter.setFavSongs(songs)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch songs: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchSongs(searchTerm: String) {


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.searchSongs(searchTerm)
                if (response.isSuccessful) {
                    val songs = response.body()?.results ?: emptyList()
                    adapter.setSongs(songs)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch songs: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onItemClick(song: Song) {
        // Create an Intent to navigate to Musicdetails activity
        val intent = Intent(this, Musicdetails::class.java)

        // Pass the song data as extras in the Intent
        intent.putExtra("trackId", song.trackId)
        intent.putExtra("trackName", song.trackName)
        intent.putExtra("artistName", song.artistName)
        intent.putExtra("previewUrl", song.previewUrl)

        // Start the Musicdetails activity
        startActivity(intent)

        // Show a toast to indicate which song was clicked
        Toast.makeText(this, "Clicked on ${song.trackName}", Toast.LENGTH_SHORT).show()
    }
    override fun onAddToFavoritesClick(song :Song){
        val sharedPreferences = getSharedPreferences("MyFavorites", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve the existing list or initialize an empty list if it doesn't exist yet
//        favoritesSet = sharedPreferences.getStringSet("favoritesSet", HashSet())?.toMutableSet() ?: HashSet()

        // Add the current song's track ID to the list of favorites
        favoritesSet.add(song.trackName)

        // Save the updated list of favorites back to SharedPreferences
        editor.putStringSet("favoritesSet", favoritesSet)
        editor.apply()

        // Show a toast to indicate the song has been added to favorites
        Toast.makeText(this, "${song.trackName} added to favorites", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if(ShowFav.visibility==View.GONE){
            ShowFav.visibility=View.VISIBLE
            searchEditText.visibility=View.VISIBLE
            adapter.clearSongs()
        }
        else{
        super.onBackPressed()}
    }


}
