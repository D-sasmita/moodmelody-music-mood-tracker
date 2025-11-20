
package com.marwadiuniversity.moodmelody.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SafeYoutubeResponseAdapter : JsonDeserializer<YoutubeSearchResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): YoutubeSearchResponse {
        val jsonObject = json?.asJsonObject
        val itemsArray = jsonObject?.getAsJsonArray("items")

        val videoItems = mutableListOf<VideoItem>()

        itemsArray?.forEach { itemElement ->
            val itemObject = itemElement.asJsonObject

            // Safely extract data, providing defaults for nulls
            val idObject = itemObject.getAsJsonObject("id")
            val videoId = idObject?.get("videoId")?.asString ?: "" // Default to empty string

            val snippetObject = itemObject.getAsJsonObject("snippet")
            val title = snippetObject?.get("title")?.asString ?: "[No Title]"
            val channelTitle = snippetObject?.get("channelTitle")?.asString ?: "[No Channel]"

            val thumbnailsObject = snippetObject?.getAsJsonObject("thumbnails")
            val highThumbnailObject = thumbnailsObject?.getAsJsonObject("high")
            val thumbnailUrl = highThumbnailObject?.get("url")?.asString ?: "" // Default to empty string

            // Create the objects with guaranteed non-null values
            val finalVideoId = VideoId(videoId)
            val finalThumbnail = Thumbnail(thumbnailUrl)
            val finalThumbnails = Thumbnails(finalThumbnail)
            val finalSnippet = Snippet(title, channelTitle, finalThumbnails)
            val finalVideoItem = VideoItem(finalVideoId, finalSnippet)

            videoItems.add(finalVideoItem)
        }

        return YoutubeSearchResponse(videoItems)
    }
}
