package com.marwadiuniversity.moodmelody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marwadiuniversity.moodmelody.R
import com.marwadiuniversity.moodmelody.api.VideoItem
import com.marwadiuniversity.moodmelody.utils.PreferencesHelper

class VideoAdapter(
    private var videos: List<VideoItem>,
    private val onVideoClick: (VideoItem) -> Unit,
    private val onFavoriteClick: (VideoItem, Int) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video, position)
    }

    override fun getItemCount(): Int = videos.size

    fun updateData(newVideos: List<VideoItem>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        private val title: TextView = itemView.findViewById(R.id.tvVideoTitle)
        private val channelName: TextView = itemView.findViewById(R.id.tvChannelName)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

        fun bind(video: VideoItem, position: Int) {
            // No need for null checks here, thanks to the safe adapter
            title.text = video.snippet.title
            channelName.text = video.snippet.channelTitle

            Glide.with(itemView.context)
                .load(video.snippet.thumbnails.high.url)
                .placeholder(R.color.cardview_dark_background)
                .into(thumbnail)

            itemView.setOnClickListener {
                onVideoClick(video)
            }

            if (PreferencesHelper.isFavorite(itemView.context, video)) {
                btnFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                btnFavorite.setImageResource(R.drawable.ic_heart_outline)
            }

            btnFavorite.setOnClickListener {
                onFavoriteClick(video, position)
            }
        }
    }
}
