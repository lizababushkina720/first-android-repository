package com.example.hw_01_sem2.ui.screen.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.hw_01_sem2.R

@Composable
fun AboutAppDialog(
    viewModel: AboutAppViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.onSheetOpened()
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.about_title_top_spacing)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = dimensionResource(id = R.dimen.about_buttons_inner_spacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.about_sheet_horizontal_padding))
                    .padding(top = dimensionResource(id = R.dimen.about_title_top_spacing)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.about_app_icon_size)),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.about_title_top_spacing)))

                Text(
                    text = stringResource(id = R.string.about_app_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.about_description_top_spacing)))

                Text(
                    text = stringResource(id = R.string.about_app_description),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.about_buttons_top_spacing)))

                Button(
                    onClick = { viewModel.onAcceptClicked() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text =  stringResource(id = R.string.about_btn_agree))

                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.about_buttons_inner_spacing)))
            }
        }
    }
}