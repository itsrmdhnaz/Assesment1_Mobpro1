package org.d3if3026.assesment1.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID")).format(date)
}