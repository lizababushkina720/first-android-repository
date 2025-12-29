package com.example.hw_05.ui.pets



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.hw_05.di.ServiceLocator
import kotlinx.coroutines.launch
import com.example.hw_05.R
import com.example.hw_05.model.PetDataModel
import com.example.hw_05.ui.nav.Routes
import java.util.Date
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.material3.LinearProgressIndicator
import com.example.hw_05.Validators


@Composable
fun AddPetScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val repo = remember { ServiceLocator.petRepository }

    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)
    val snackbarTopPadding = dimensionResource(R.dimen.v_space_8)

    var name by rememberSaveable { mutableStateOf("") }
    var weightText by rememberSaveable { mutableStateOf("") }
    var loading by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }


    val nameOk = name.trim().isNotBlank()
    val weightOk = Validators.parseWeightKg(weightText) != null
    val formOk = nameOk && weightOk

    val nameError = if (showErrors && !nameOk) stringResource(R.string.error_required) else null
    val weightError = if (showErrors && !weightOk) stringResource(R.string.error_invalid_weight) else null

    fun submit() {
        scope.launch {
            showErrors = true
            if (!formOk) {
                snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_add_pet_bad_input))
                return@launch
            }

            val petName = name.trim()
            val weight = Validators.parseWeightKg(weightText)!!

            try {
                loading = true
                repo.addPet(
                    pet = PetDataModel(
                        id = 0,
                        name = petName,
                        weightKg = weight,
                        appearedAt = Date()
                    )
                )


                navController.navigate(Routes.ADD_PET_OK) {
                    popUpTo(Routes.ADD_PET) { inclusive = true }
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                loading = false
                snackbarHostState.showSnackbar(
                    ctx.getString(R.string.snackbar_add_pet_failed) + ": " + (e.message ?: "")
                )
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        Scaffold(
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .imePadding()
                    .padding(screenPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(vSpace12)
            ) {
                if (loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = stringResource(R.string.add_pet_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.add_pet_field_name)) },
                    singleLine = true,
                    isError = nameError != null,
                    supportingText = { if (nameError != null) Text(nameError) }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text(stringResource(R.string.add_pet_field_weight)) },
                    singleLine = true,
                    isError = weightError != null,
                    supportingText = { if (weightError != null) Text(weightError) }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    contentPadding = PaddingValues(vertical = btnPaddingV),
                    onClick = { submit() }
                ) {
                    Text(
                        text = if (loading) {
                            stringResource(R.string.add_pet_btn_save_loading)
                        } else {
                            stringResource(R.string.add_pet_btn_save)
                        }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    contentPadding = PaddingValues(vertical = btnPaddingV),
                    onClick = { navController.popBackStack() }
                ) {
                    Text(stringResource(R.string.common_btn_back))
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = snackbarTopPadding)
        )
    }
}
