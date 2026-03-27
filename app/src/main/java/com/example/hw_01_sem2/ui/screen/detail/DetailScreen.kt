package com.example.hw_01_sem2.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.TopAppBar as MaterialTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.hw_01_sem2.R
import com.example.domain.model.OrganizationModel

@Composable
fun DetailScreen(
    organization: OrganizationModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MaterialTopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.screen_padding))
        ) {
            Text(
                text = organization.shortName.ifEmpty { organization.fullName },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.detail_title_bottom_spacing)))

            DetailItem(
                icon = stringResource(R.string.icon_address),
                label = stringResource(R.string.address),
                value = organization.address
            )
            DetailItem(
                icon = stringResource(R.string.icon_inn),
                label = stringResource(R.string.inn),
                value = organization.inn
            )
            DetailItem(
                icon = stringResource(R.string.icon_kpp),
                label = stringResource(R.string.kpp),
                value = organization.kpp
            )
            DetailItem(
                icon = stringResource(R.string.icon_ogrn),
                label = stringResource(R.string.ogrn),
                value = organization.ogrn
            )

            if (!organization.managementName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.detail_section_spacing)))

                Text(
                    text = stringResource(R.string.management),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${organization.managementPost ?: ""} ${organization.managementName}".trim(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: String,
    label: String,
    value: String?
) {
    if (value.isNullOrBlank()) return

    Row(
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.detail_item_vertical_spacing)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.detail_icon_spacing))
    ) {
        Text(
            text = icon,
            modifier = Modifier.width(dimensionResource(R.dimen.detail_icon_width)),
            style = MaterialTheme.typography.bodyLarge
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}