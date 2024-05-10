package org.d3if3026.assesment1.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3026.assesment1.database.MovieDao
import org.d3if3026.assesment1.model.Movie

class MainViewModel(dao: MovieDao) : ViewModel() {

    val data: StateFlow<List<Movie>> = dao.getMovie().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}