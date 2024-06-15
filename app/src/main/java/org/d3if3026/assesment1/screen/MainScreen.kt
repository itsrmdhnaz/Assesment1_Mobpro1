package org.d3if3026.assesment1.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.d3if3026.assesment1.util.SettingsDataStore


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val dataStore = SettingsDataStore(LocalContext.current)
    val isDark by dataStore.darkModeFlow.collectAsState(false)
//    MainScreen(navController = rememberNavController(), isDark, dataStore)
}


