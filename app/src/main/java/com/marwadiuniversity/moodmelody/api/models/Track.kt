package com.marwadiuniversity.moodmelody.api.models

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("artists")
    val artists: List<Artist>,

    @SerializedName("album")
    val album: Album,

    @SerializedName("duration_ms")
    val durationMs: Int,

    @SerializedName("preview_url")
    val previewUrl: String?,

    @SerializedName("uri")
    val uri: String
) {
    // Helper function to get artist names
    fun getArtistNames(): String {
        return artists.joinToString(", ") { it.name }
    }

    // Helper function to get album image
    fun getAlbumImageUrl(): String? {
        return album.images.firstOrNull()?.url
    }
}

data class Artist(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)

data class Album(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("images")
    val images: List<AlbumImage>
)

data class AlbumImage(
    @SerializedName("url")
    val url: String,

    @SerializedName("height")
    val height: Int?,

    @SerializedName("width")
    val width: Int?
)