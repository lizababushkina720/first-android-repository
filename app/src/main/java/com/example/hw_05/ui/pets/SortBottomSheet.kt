package com.example.hw_05.ui.pets

import com.example.hw_05.Keys
import com.example.hw_05.R


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: String,
    onDismiss: () -> Unit,
    onPick: (String) -> Unit
) {
    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(vSpace12)
        ) {
            Text(stringResource(R.string.sort_title))

            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = btnPaddingV),
                enabled = currentSort != Keys.SORT_NEWEST,
                onClick = { onPick(Keys.SORT_NEWEST) }
            ) {
                Text(stringResource(R.string.sort_newest))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = btnPaddingV),
                enabled = currentSort != Keys.SORT_WEIGHT,
                onClick = { onPick(Keys.SORT_WEIGHT) }
            ) {
                Text(stringResource(R.string.sort_weight))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = btnPaddingV),
                enabled = currentSort != Keys.SORT_ALPHA,
                onClick = { onPick(Keys.SORT_ALPHA) }
            ) {
                Text(stringResource(R.string.sort_alpha))
            }
        }
    }
}
