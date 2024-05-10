package org.d3if3026.assesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3026.assesment1.model.Movie

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY releaseDate DESC")
    fun getMovie(): Flow<List<Movie>>

}