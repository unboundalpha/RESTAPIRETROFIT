package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class SongAdapter(
    private val songs: MutableList<Song>,
    private val listener: OnItemClickListener,
    private var favoritesSet: MutableSet<String>,
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(song: Song)
        fun onAddToFavoritesClick(song: Song)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
        val addFAv: ImageButton = itemView.findViewById(R.id.AddFav)

        init {
            itemView.setOnClickListener(this)
            addFAv.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.AddFav -> {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val song = songs[position]
                        // Hide the AddFav button
                        addFAv.visibility = View.GONE
                        // Handle adding to favorites here, for example:
                        // You can pass this information to the listener if needed
                        listener.onAddToFavoritesClick(song)
                    }
                }
                else -> {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val song = songs[position]
                        listener.onItemClick(song)
                    }
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.titleTextView.text = song.trackName
        holder.artistTextView.text = song.artistName
        if(favoritesSet.contains(song.trackName)){
            holder.addFAv.visibility=View.GONE
        }
        else
            holder.addFAv.visibility=View.VISIBLE
    }

    override fun getItemCount(): Int {
        return songs.size

    }

    fun setSongs(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }
    fun setFavSongs(Newsongs: List<Song>){
        Log.d("XYZABC","TravkName"+Newsongs)
        songs.add(Newsongs[0])
        this.favoritesSet.add(Newsongs[0].trackName)
        notifyDataSetChanged()
    }
    fun clearSongs(){
        songs.clear()
//        songs.addAll(newSongs)
        notifyDataSetChanged()
    }

}
