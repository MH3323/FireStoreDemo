package com.example.firestoredemo

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.firestoredemo.ui.theme.FireStoreDemoTheme
import com.google.common.math.Quantiles.scale
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
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = viewModel<HomePageViewModel>()
) {
    val state = homePageViewModel.state.value
    val currentUser = Firebase.auth.currentUser


    Column {
        if (currentUser != null) {
            homePageViewModel.getUserInformation(currentUser.uid)
            val userInformation by homePageViewModel.userInformation.collectAsState()
            val userImage by homePageViewModel.userImage.collectAsState()

            AppBarWithUserImage(
                username = userInformation?.fullName ?: "EMPTY",
                userImage = userImage
            )
//            Text(
//                text = userInformation.toString()
//            )
        } else {
            Text(
                text = state.error
            )
        }
    }

}

