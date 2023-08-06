package com.example.firestoredemo

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.example.firestoredemo.ui.theme.FireStoreDemoTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun ImageFromDatabaseDisplay(imageUrl: String) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .build()

    val painter: Painter = rememberImagePainter(
        data = imageUrl,
        imageLoader = imageLoader,
        builder = {
            crossfade(true)
            scale(Scale.FILL)
        }
    )

    Image(
        painter = painter,
        contentDescription = "User image",
        modifier = Modifier
            .size(100.dp)
    )
}

@Composable
fun JobList(
    jobs: List<Job>,
    onUserClicked: (String) -> Unit,
    modifier: Modifier = Modifier
        .height(250.dp)
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(jobs) { job ->
            JobCard(
                job = job,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onUserClicked(job.id)
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithUserImage(
    username: String = "DEFAULT USERNAME",
    userImage: String? = null,
) {
    Log.d("userimage1", userImage.toString())

    Surface(
    ) {
        TopAppBar (
            title = {
                Text(
                    text = username
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        //
                    }
                )  {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
            },
            actions = {
                userImage?.let {
                    ImageFromDatabaseDisplay(imageUrl = it)
                } ?: Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "default user image"
                )
            }
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun AppBarPreview() {
    FireStoreDemoTheme {
        AppBarWithUserImage()
    }
}

@Composable
fun JobCard(
    job: Job = Job(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription =  "Company image",
                    modifier = Modifier
                        .size(50.dp)
                )
                Column() {
                    Text(
                        text = "Job name: " + job.name
                    )
                    Text(
                        text = "Company Name: " + job.companyName
                    )
                }
            }
        }
}

@Composable
fun PDFSelectButton(
    setPdfUri: (Uri) -> Unit,
    uploadPDFToFirebase: () -> Unit
) {

    val pickPDFLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Log.d("PDF", uri.toString())
                setPdfUri(uri)

                uploadPDFToFirebase()
            }
    }

    Button(onClick = {
        pickPDFLauncher.launch("application/pdf")
    }) {
        Text(text = "Select PDF")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = viewModel<HomePageViewModel>(),
    navController: NavController
) {
    val state = homePageViewModel.state.value
    val currentUser = Firebase.auth.currentUser

    val isLoading by homePageViewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isLoading, { homePageViewModel.loadStuff()})

    val searchText by homePageViewModel.searchText.collectAsState()
    val jobs by homePageViewModel.jobs.collectAsState()
    val isSearching by homePageViewModel.isSearching.collectAsState()

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        Column {
                if (currentUser != null) {
                    homePageViewModel.getUserInformation(currentUser.uid)
                    val userInformation by homePageViewModel.userInformation.collectAsState()
                    val userImage by homePageViewModel.userImage.collectAsState()

                    AppBarWithUserImage(
                        username = userInformation?.fullName ?: "EMPTY",
                        userImage = userImage
                    )

                    TextField(
                        value = searchText,
                        onValueChange = homePageViewModel::onSearchTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search"
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSearching) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        JobList(
                            jobs = jobs,
                            onUserClicked = {
                                navController.navigate("jobs/" + it)
                            }
                        )
                    }


                    PDFSelectButton(
                        setPdfUri = { uri ->
                            homePageViewModel.setCV(uri)
                        },
                        uploadPDFToFirebase = {
                            homePageViewModel.uploadPDF()
                        }
                    )
                } else {
                    Text(
                        text = state.error
                    )
                }
            }
                PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
}

