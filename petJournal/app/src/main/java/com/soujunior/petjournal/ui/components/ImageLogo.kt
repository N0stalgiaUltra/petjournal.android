package com.soujunior.petjournal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soujunior.petjournal.R

/*
@Composable
fun ImageLogo(
    modifier: Modifier = Modifier
        .size(width = 150.dp, height = 150.dp)
        .padding(top = 20.dp),
    darkMode: Boolean = isSystemInDarkTheme()
) {
    val imageLight = painterResource(id = R.drawable.logo_purple)
    val imageDark = painterResource(id = R.drawable.logo_pink)
    val image = if (darkMode) imageDark else imageLight

    Image(
        painter = image,
        contentDescription = "Imagem logo",
        modifier = modifier
    )
}*/


@Composable
fun ImageLogo(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .padding(top = 20.dp),
    darkMode: Boolean = isSystemInDarkTheme()
) {
    val imageLight = painterResource(id = R.drawable.logo_purple)
    val imageDark = painterResource(id = R.drawable.logo_pink)
    val image = if (darkMode) imageDark else imageLight

    Image(
        painter = image,
        contentDescription = "Imagem logo",
        modifier = modifier
    )
}
