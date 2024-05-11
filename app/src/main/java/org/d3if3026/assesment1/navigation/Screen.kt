package org.d3if3026.assesment1.navigation

import org.d3if3026.assesment1.screen.KEY_ID_MOVIE

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object About : Screen("about")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_MOVIE}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}