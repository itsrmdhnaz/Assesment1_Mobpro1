package org.d3if3026.assesment1.screen

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.d3if3026.assesment1.R
import org.d3if3026.assesment1.database.MovieDb
import org.d3if3026.assesment1.helper.formatDate
import org.d3if3026.assesment1.ui.theme.Assesment1Theme
import org.d3if3026.assesment1.util.ViewModelFactory
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

const val KEY_ID_MOVIE = "idMovie"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = MovieDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    val currentDate by remember { mutableStateOf(Date()) }

    /**
     * State input
     */
    var title by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf("") }
    var linkUri by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var releaseDate by rememberSaveable { mutableStateOf(currentDate) }
    var review by rememberSaveable { mutableStateOf("") }
    var isWatching by rememberSaveable { mutableStateOf(false) }
    var director by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageUri = uri?.toString() ?: ""
        }
    )

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getMovie(id) ?: return@LaunchedEffect
        title = data.title
        linkUri = data.linkUri.toString()
        duration = data.duration.toString()
        genre = data.genre
        releaseDate = data.releaseDate
        director = data.director.toString()
        review = data.review.toString()
        isWatching = data.isWatching
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if(id == null){
                        Text(text = stringResource(id = R.string.add_movie))
                    } else {
                        Text(text = stringResource(id = R.string.edit_movie))
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {

                        if(title == "" || duration == "" || duration == "0" || genre == ""){
                            Toast.makeText(context,
                                context.getString(R.string.title_duration_director_dan_genre_can_not_be_empty),
                                Toast.LENGTH_LONG
                            ).show()
                            return@IconButton
                        }

                        if(id == null){
                            viewModel.insert(title, imageUri, linkUri, duration.toInt(), genre, director, releaseDate, review, isWatching)
                        } else {
                            viewModel.update(id, title, imageUri, linkUri, duration.toInt(), genre, director, releaseDate, review, isWatching)
                        }

                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.save),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormMovie(
            title = title,
            onTitleChange = { title = it },
            imageUri = imageUri,
            onimageUriChange = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            linkUri = linkUri,
            onLinkUriChange = { linkUri = it },
            duration = duration,
            onDurationChange = { duration = it },
            genre = genre,
            onGenreChange = { genre = it },
            releaseDate = releaseDate,
            onReleaseDateChange = { releaseDate = it },
            review = review,
            onReviewChange = { review = it },
            director = director,
            onDirectorChange = { director = it },
            isWatching = isWatching,
            onIsWatchingChange = { isWatching = it },
            modifier = Modifier.padding(padding)
        )
    }
}


@Composable
fun FormMovie(
    title: String, onTitleChange: (String) -> Unit,
    imageUri: String, onimageUriChange: () -> Unit,
    linkUri: String, onLinkUriChange: (String) -> Unit,
    duration: String, onDurationChange: (String) -> Unit,
    genre: String, onGenreChange: (String) -> Unit,
    releaseDate: Date, onReleaseDateChange: (Date) -> Unit,
    director: String, onDirectorChange: (String) -> Unit,
    review: String, onReviewChange: (String) -> Unit,
    isWatching: Boolean, onIsWatchingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var dateString by remember(releaseDate) {
        mutableStateOf(
            formatDate(releaseDate)
        )
    }

    var date: Date

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .height(250.dp)
                    .width(200.dp)
                    .clickable {
                        onimageUriChange()
                    }
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box {
                    if (imageUri != "") {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.broken_img),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Text(
                    text = "Add poster Movie",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.have_you_watched_it),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = isWatching,
                onCheckedChange = { onIsWatchingChange(it) }
            )
        }

        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = linkUri,
            onValueChange = { onLinkUriChange(it) },
            label = { Text(text = stringResource(R.string.link_url)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                if (linkUri != "") {
                    IconButton(onClick = {

                        val urlIntent = Intent(
                            Intent.ACTION_VIEW,
                          if(linkUri.startsWith("https://")) Uri.parse("https://$linkUri") else Uri.parse("https://www.google.com/search?q=$linkUri")
                        )

                        context.startActivity(urlIntent)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = ""
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = duration,
            onValueChange = { onDurationChange(it) },
            label = { Text(text = stringResource(R.string.minute)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(modifier = Modifier
                .fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            date = GregorianCalendar(year, month, dayOfMonth).time

                            onReleaseDateChange(date)
                            dateString = formatDate(date)

                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Release Date ")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = dateString, style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
                    }
                }
            }
        }

        OutlinedTextField(
            value = genre,
            onValueChange = { onGenreChange(it) },
            label = { Text(text = stringResource(R.string.genre)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = director,
            onValueChange = { onDirectorChange(it) },
            label = { Text(text = stringResource(R.string.director)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = review,
            onValueChange = { onReviewChange(it) },
            label = { Text(text = stringResource(R.string.review)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.other),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.delete_movie))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun DisplayAlertDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.delete_movie)) },
            confirmButton = {
                TextButton(onClick = { onConfirmation() }) {
                    Text(text = stringResource(R.string.button_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.button_cancel))
                }
            },
            onDismissRequest = { onDismissRequest() }
        )
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment1Theme {
        DetailScreen(rememberNavController())
    }
}