package org.d3if3026.assesment1.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3026.assesment1.model.Movie
import org.d3if3026.assesment1.network.ApiStatus
import org.d3if3026.assesment1.network.MovieApi
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Movie>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = MovieApi.service.getMovie(email).data
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure retrieve data: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(email: String, title: String, genre: String, releaseDate: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MovieApi.service.postMovie(
                    email,
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    genre.toRequestBody("text/plain".toMediaTypeOrNull()),
                    releaseDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody(),
                )

                if (result.status == "success")
                    retrieveData(email)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure save: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun deleteData(email: String, movieId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MovieApi.service.deleteMovie(
                    email,
                    movieId
                )

                if (result.status == "success")
                    retrieveData(email)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { errorMessage.value = null }
}
