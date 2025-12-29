package com.example.hw_05.ui.pets

import com.example.hw_05.R


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.hw_05.Keys
import com.example.hw_05.data.prefs.SortStore
import com.example.hw_05.di.ServiceLocator
import kotlinx.coroutines.launch
import com.example.hw_05.model.PetDataModel
import com.example.hw_05.ui.nav.Routes
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.OutlinedTextField



@Composable
fun PetsListScreen(
    navController: NavHostController
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val repo = remember { ServiceLocator.petRepository }
    val sortStore = remember { SortStore() }

    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val vSpace8 = dimensionResource(R.dimen.v_space_8)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)
    val cardPadding = dimensionResource(R.dimen.card_padding)

    var loading by rememberSaveable { mutableStateOf(false) }
    var pets by rememberSaveable { mutableStateOf(emptyList<PetDataModel>()) }
    var showSortSheet by rememberSaveable { mutableStateOf(false) }
    var currentSort by rememberSaveable { mutableStateOf(sortStore.getSort()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val filteredPets = remember(pets, searchQuery) {
        val q = searchQuery.trim().lowercase(Locale.getDefault())
        if (q.isBlank()) pets
        else pets.filter { it.name.lowercase(Locale.getDefault()).contains(q) }
    }



    fun currentSortTitle(): String = when (currentSort) {
        Keys.SORT_WEIGHT -> ctx.getString(R.string.sort_weight)
        Keys.SORT_ALPHA -> ctx.getString(R.string.sort_alpha)
        else -> ctx.getString(R.string.sort_newest)
    }



    fun reloadPets() {

        scope.launch {
            try {
                loading = true
                pets = repo.getPets(sort = currentSort)
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(
                    ctx.getString(R.string.snackbar_load_pets_failed) + ": " + (e.message ?: "")
                )
            } finally {
                loading = false
            }
        }
    }


    LaunchedEffect(currentSort) {
        reloadPets()
    }

    if (showSortSheet) {
        SortBottomSheet(
            currentSort = currentSort,
            onDismiss = { showSortSheet = false },
            onPick = { picked ->
                currentSort = picked
                sortStore.setSort(picked)
                showSortSheet = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(vSpace12)
        ) {
            Text(
                text = stringResource(R.string.pets_title),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true,
                label = { Text(stringResource(R.string.search)) }
            )


            Text(
                text = ctx.getString(R.string.pets_sort_current_format, currentSortTitle()),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = { showSortSheet = true }
            ) {
                Text(stringResource(R.string.pets_btn_sort))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = { navController.navigate(Routes.ADD_PET) }
            ) {
                Text(stringResource(R.string.pets_btn_add))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = { navController.navigate(Routes.PROFILE) }
            ) {
                Text(stringResource(R.string.pets_btn_profile))
            }

            if (pets.isEmpty() && !loading) {
                Text(stringResource(R.string.pets_empty))
                return@Column
            }
            if (!loading && pets.isNotEmpty() && filteredPets.isEmpty()) {
                Text(stringResource(R.string.nothing_was_found))
                return@Column
            }



            val df = remember { android.text.format.DateFormat.getDateFormat(ctx) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(vSpace8)
            ) {
                items(filteredPets) { pet ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(cardPadding),
                            verticalArrangement = Arrangement.spacedBy(vSpace8)
                        ) {
                            Text(text = pet.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = ctx.getString(R.string.pets_item_weight_format, pet.weightKg),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = ctx.getString(R.string.pets_item_date_format, df.format(pet.appearedAt)),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
