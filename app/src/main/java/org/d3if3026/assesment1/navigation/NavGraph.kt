package org.d3if3026.assesment1.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3026.assesment1.screen.AboutScreen
import org.d3if3026.assesment1.screen.DetailScreen
import org.d3if3026.assesment1.screen.KEY_ID_MOVIE
import org.d3if3026.assesment1.screen.MainScreen
import org.d3if3026.assesment1.util.SettingsDataStore


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
    isDark: Boolean,
    dataStore: SettingsDataStore
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController, isDark, dataStore)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_MOVIE) { type = NavType.LongType }
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_MOVIE)
            DetailScreen(navController, id)
        }
    }
}