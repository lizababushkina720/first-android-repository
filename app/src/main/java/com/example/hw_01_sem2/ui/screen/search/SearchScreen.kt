package com.example.hw_01_sem2.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.hw_01_sem2.R
import com.example.domain.model.OrganizationModel
import com.example.hw_01_sem2.ui.components.OrganizationCard
import androidx.compose.material.TopAppBar as MaterialTopAppBar

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onOrganizationClick: (OrganizationModel) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val snackbarEvent by viewModel.snackbarEvent.collectAsState()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(snackbarEvent) {
        snackbarEvent?.let { event ->
            val messageResId = when (event) {
                is SearchSnackbarEvent.DataFromCache -> R.string.data_from_cache
                is SearchSnackbarEvent.DataFromServer -> R.string.data_from_server
                is SearchSnackbarEvent.Error -> {
                    when (event.messageKey) {
                        SearchViewModel.UNKNOWN_ERROR -> R.string.unknown_error
                        else -> R.string.error_loading
                    }
                }
            }
            snackbarHostState.showSnackbar(context.resources.getString(messageResId))
            viewModel.clearSnackbarEvent()
        }
    }

    Scaffold(
        topBar = {
            MaterialTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.search_screen_padding)),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = viewModel::clearQuery) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear))
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.performSearch() })
            )

            Button(
                onClick = viewModel::performSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.search_screen_padding))
            ) {
                Text(stringResource(R.string.search))
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.search_button_spacing)))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.search_screen_padding))
            ) {
                when (val state = uiState) {
                    is SearchUiState.Idle -> {
                        EmptyState(message = stringResource(R.string.initial_search_message))
                    }

                    is SearchUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is SearchUiState.Success -> {
                        val organizations = state.organizations
                        if (organizations.isEmpty()) {
                            EmptyState(message = stringResource(R.string.no_results))
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(
                                    dimensionResource(R.dimen.card_vertical_spacing)
                                ),
                                contentPadding = PaddingValues(
                                    bottom = dimensionResource(R.dimen.list_bottom_padding)
                                )
                            ) {
                                items(organizations, key = { it.inn }) { organization ->
                                    OrganizationCard(
                                        organization = organization,
                                        onClick = { onOrganizationClick(organization) }
                                    )
                                }
                            }
                        }
                    }

                    is SearchUiState.Error -> {
                        val errorMessageResId = when (state.messageKey) {
                            SearchViewModel.UNKNOWN_ERROR -> R.string.unknown_error
                            else -> R.string.error_loading
                        }
                        ErrorState(
                            message = stringResource(errorMessageResId),
                            onRetry = viewModel::performSearch
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.error_button_spacing)))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}