package org.d3if3026.assesment1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val imageUri: String? = null,
    val linkUri: String? = null,
    val duration: Int,
    val genre: List<Int>,
    val director: String? = null,
    val releaseDate: Date,
    val review: String? = null,
    val isWatching: Boolean
)