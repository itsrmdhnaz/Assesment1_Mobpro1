package org.d3if3026.assesment1.model

data class OpStatus(
    var status: String,
    var message: String?,
    var data: List<Movie> = emptyList()
)