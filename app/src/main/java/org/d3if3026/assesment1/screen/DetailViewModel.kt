package org.d3if3026.assesment1.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3026.assesment1.database.MovieDao
import org.d3if3026.assesment1.model.Movie
import java.util.Date

class DetailViewModel(private val dao: MovieDao) : ViewModel() {

    fun insert(
        title: String,
        imageUri: String,
        linkUri: String,
        duration: Int,
        genre: List<Int>,
        director: String,
        releaseDate: Date,
        review: String,
        isWatching: Boolean
        ) {
        val movie = Movie(
            title = title,
            imageUri = imageUri,
            linkUri = linkUri,
            duration = duration,
            genre = genre,
            director = director,
            releaseDate = releaseDate,
            review = review,
            isWatching = isWatching
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(movie)
        }
    }

    suspend fun getMovie(id: Long): Movie? {
        return dao.getCatatanById(id)
    }

    fun update(
        id: Long,
        title: String,
        imageUri: String,
        linkUri: String,
        duration: Int,
        genre: List<Int>,
        director: String,
        releaseDate: Date,
        review: String,
        isWatching: Boolean
    ) {
        val movie = Movie(
            id = id,
            title = title,
            imageUri = imageUri,
            linkUri = linkUri,
            duration = duration,
            genre = genre,
            director = director,
            releaseDate = releaseDate,
            review = review,
            isWatching = isWatching
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(movie)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}