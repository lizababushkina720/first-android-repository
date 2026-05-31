package com.example.hw_01_sem2.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar as MaterialTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.hw_01_sem2.R
import com.example.domain.model.OrganizationModel
import com.example.hw_01_sem2.ui.custom.FirstCustomView
import com.example.hw_01_sem2.ui.theme.CharDarkPurple
import com.example.hw_01_sem2.ui.theme.ChartDarkRed
import com.example.hw_01_sem2.ui.theme.ChartGold
import com.example.hw_01_sem2.ui.theme.ChartLightRose
import com.example.hw_01_sem2.ui.theme.ChartRose

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()


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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.Error -> {
                    Text(
                        text = stringResource(R.string.error),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is DetailUiState.Success -> {
                    OrganizationDetailContent(organization = state.organization)
                }
            }
        }
    }
}

@Composable
fun OrganizationDetailContent(organization: OrganizationModel) {
    val chartValues = integerArrayResource(R.array.organization_chart_values).toList()
    val chartColors = listOf(CharDarkPurple, ChartDarkRed, ChartRose, ChartLightRose, ChartGold)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding))
            .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.chart_section_margin)))

            Text(
                text = stringResource(R.string.distribution_statistics),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.chart_title_margin)))

            FirstCustomView(
                values = chartValues,
                colors = chartColors
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.chart_section_margin)))
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