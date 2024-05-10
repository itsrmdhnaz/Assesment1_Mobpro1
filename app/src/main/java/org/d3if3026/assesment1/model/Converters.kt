package org.d3if3026.assesment1.model

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromGenreList(genreList: List<Int>): String {
        val genreListString = mutableListOf<String>()
        for(genre in genreList){
            genreListString.add(genre.toString())
        }
        return genreList.joinToString(",")
    }

    @TypeConverter
    fun toGenreList(genreString: String): List<Int> {
        return genreString.split(",").map { it.toInt() }
    }
}