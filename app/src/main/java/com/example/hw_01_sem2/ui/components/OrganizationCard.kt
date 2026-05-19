package com.example.hw_01_sem2.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hw_01_sem2.R
import com.example.domain.model.OrganizationModel

@Composable
fun OrganizationCard(
    organization: OrganizationModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.default_elevation)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding))
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = organization.shortName.ifEmpty { organization.fullName },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                StatusChip(status = organization.status)
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_title_to_inn_spacing)))

            Text(
                text = stringResource(R.string.inn_prefix) + organization.inn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!organization.address.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_inn_to_address_spacing)))
                Text(
                    text = organization.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_bottom_spacing)))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(organization.type ?: stringResource(R.string.legal_entity))
                    }
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: String?) {
    val isActive = status?.equals(stringResource(R.string.active), ignoreCase = true) == true

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = if (isActive)
                    stringResource(R.string.status_active)
                else
                    stringResource(R.string.status_liquidated),
                color = if (isActive)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    )
}