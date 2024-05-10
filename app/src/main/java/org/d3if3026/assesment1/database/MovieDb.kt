package org.d3if3026.assesment1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.d3if3026.assesment1.model.Converters
import org.d3if3026.assesment1.model.Movie


@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDb : RoomDatabase() {

    abstract val dao: MovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDb? = null

        fun getInstance(context: Context): MovieDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDb::class.java,
                        "movie.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}