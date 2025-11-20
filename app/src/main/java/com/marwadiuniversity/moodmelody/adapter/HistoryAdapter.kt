package com.marwadiuniversity.moodmelody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marwadiuniversity.moodmelody.R
import com.marwadiuniversity.moodmelody.utils.MoodHistoryItem
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val historyItems: List<MoodHistoryItem>,
    private val onItemClick: (MoodHistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMoodEmoji: TextView = itemView.findViewById(R.id.tvMoodEmoji)
        val tvMoodName: TextView = itemView.findViewById(R.id.tvMoodName)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp) // Add this TextView in item_history.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyItems[position]

        holder.tvMoodName.text = item.mood
        holder.tvMoodEmoji.text = getEmojiForMood(item.mood)

        // Format timestamp
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
        holder.tvTimestamp.text = sdf.format(java.util.Date(item.timestamp))

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = historyItems.size

    private fun getEmojiForMood(mood: String): String {
        return when (mood.lowercase()) {
            "happy" -> "😊"
            "sad" -> "😢"
            "energetic" -> "🔥"
            "calm" -> "🌙"
            "chill" -> "😎"
            "romantic" -> "💗"
            "party" -> "🎉"
            "relaxed" -> "😌"
            else -> "🎵"
        }
    }
}



    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val now = Calendar.getInstance()

        val calendar = Calendar.getInstance()
        calendar.time = date

        val sameDay = now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

        val yesterday = now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return when {
            sameDay -> "Today, ${timeFormat.format(date)}"
            yesterday -> "Yesterday, ${timeFormat.format(date)}"
            else -> SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
        }
    }

