package org.d3if3026.assesment1.model

import java.util.Date

data class Movie(
    val id: Long,
    val title: String,
    val imageUri: String? = null,
    val linkUri: String? = null,
    val duration: Int,
    val genre: List<String>,
    val director: String? = "",
    val releaseDate: Date,
    val review: String,
    val isWatching: Boolean
)