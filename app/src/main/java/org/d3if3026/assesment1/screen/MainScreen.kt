package org.d3if3026.assesment1.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3026.assesment1.R
import org.d3if3026.assesment1.helper.formatDate
import org.d3if3026.assesment1.model.Movie
import org.d3if3026.assesment1.navigation.Screen
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_app))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_movie),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val data = listOf(
        Movie(
            id = 1,
            title = "Inception",
            duration = 148,
            genre = listOf("Action", "Adventure", "Sci-Fi"),
            director = "Christopher Nolan",
            releaseDate = Date(), // Year is 1900-based, so 2010 is represented by 110
            review = "Inception is a mind-bending thriller that keeps you on the edge of your seat.",
            isWatching = true
        ),
        Movie(
            id = 1,
            title = "Inception",
            duration = 148,
            genre = listOf("Action", "Adventure", "Sci-Fi"),
            director = "Christopher Nolan",
            releaseDate = Date(), // Year is 1900-based, so 2010 is represented by 110
            review = "Inception is a mind-bending thriller that keeps you on the edge of your seat.",
            isWatching = true
        ),
        Movie(
            id = 1,
            title = "Inception",
            duration = 148,
            genre = listOf("Action", "Adventure", "Sci-Fi"),
            director = "Christopher Nolan",
            releaseDate = Date(), // Year is 1900-based, so 2010 is represented by 110
            review = "Inception is a mind-bending thriller that keeps you on the edge of your seat.",
            isWatching = true
        )
    )


    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.empty_list))
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            items(data) {
                ListItem(movie = it) {}
            }
        }
    }
}

@Composable
fun ListItem(movie: Movie, onClick: () -> Unit) {

    val calendar = Calendar.getInstance()
    calendar.time = movie.releaseDate
    val releaseYear = calendar.get(Calendar.YEAR)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        /**
         * Poster movie
         */
        Box {
            Image(
                painter = painterResource(id = R.drawable.dsc_2305_2_copy),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "${movie.title} $releaseYear",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatDate(movie.releaseDate),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}


