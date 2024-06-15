package org.d3if3026.assesment1.network

import org.d3if3026.assesment1.model.OpStatus

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://rest-api-itsrmdhnaz-moviememo.000webhostapp.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MovieApiService {
    @GET("movies.php")
    suspend fun getMovie(
        @Header("Authorization") userId: String
    ): OpStatus


    @Multipart
    @POST("movies.php")
    suspend fun postMovie(
        @Header("Authorization") email: String,
        @Part("title") title: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("releaseDate") releaseDate: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @FormUrlEncoded
    @POST("deleteArt.php")
    suspend fun deleteMovie(
        @Header("Authorization") email: String,
        @Field("id") MovieId: String
    ): OpStatus
}

object MovieApi {
    val service: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    fun getArtUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }

