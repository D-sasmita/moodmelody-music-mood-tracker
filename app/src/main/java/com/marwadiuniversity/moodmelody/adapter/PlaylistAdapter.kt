package com.marwadiuniversity.moodmelody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marwadiuniversity.moodmelody.R
import com.marwadiuniversity.moodmelody.model.Playlist
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistAdapter(
    private var playlists: MutableList<Playlist>,
    private val onPlayClick: (Playlist) -> Unit,
    private val onFavoriteClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAlbumCover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
        val tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        val tvSongInfo: TextView = itemView.findViewById(R.id.tvSongInfo)
        val fabPlay: FloatingActionButton = itemView.findViewById(R.id.fabPlay)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        holder.tvPlaylistTitle.text = playlist.title
        holder.tvGenre.text = playlist.genre
        holder.tvSongInfo.text = "${playlist.songCount} songs • ${playlist.duration}"

        // Load album cover (placeholder for now)
        // holder.ivAlbumCover.setImageResource(R.drawable.placeholder_album)

        updateFavoriteIcon(holder.btnFavorite, playlist.isFavorite)

        holder.fabPlay.setOnClickListener { onPlayClick(playlist) }

        holder.btnFavorite.setOnClickListener {
            playlist.isFavorite = !playlist.isFavorite
            updateFavoriteIcon(holder.btnFavorite, playlist.isFavorite)
            onFavoriteClick(playlist)
        }

        holder.itemView.setOnClickListener { onPlayClick(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    private fun updateFavoriteIcon(button: ImageButton, isFavorite: Boolean) {
        button.setImageResource(
            if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        )
    }

    /** Update the adapter data dynamically */
    fun updateData(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}
