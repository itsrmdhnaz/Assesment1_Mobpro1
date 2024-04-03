package org.d3if3026.assesment1.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3026.assesment1.model.UserModel
import org.d3if3026.assesment1.screen.MainScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), userModel: UserModel = UserModel()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route){
            MainScreen(navController, UserModel())
        }
        composable(route = Screen.About.route){
//            AboutScreen(navController)
        }
    }
}