package org.d3if3026.assesment1.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object About : Screen("about")
}