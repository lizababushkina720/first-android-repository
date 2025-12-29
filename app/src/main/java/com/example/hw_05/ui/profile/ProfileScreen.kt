package com.example.hw_05.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.hw_05.R
import com.example.hw_05.data.prefs.SessionStore
import com.example.hw_05.di.ServiceLocator
import com.example.hw_05.ui.nav.Routes
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val ctx = LocalContext.current

    val repo = remember { ServiceLocator.userRepository }
    val sessionStore = remember { SessionStore() }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)

    var loading by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var user by rememberSaveable { mutableStateOf<com.example.hw_05.model.UserDataModel?>(null) }


    val userId = remember { sessionStore.getCurrentUserId() }

    LaunchedEffect(userId) {
        if (userId == null) return@LaunchedEffect
        try {
            loading = true
            user = repo.findById(userId)
            title = if (user == null) {
                ctx.getString(R.string.profile_user_not_found)
            } else {
                ctx.getString(R.string.profile_user_format, user!!.name, user!!.nickname, user!!.email)
            }
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(
                ctx.getString(R.string.snackbar_profile_load_failed) + ": " + (e.message ?: "")
            )
        } finally {
            loading = false
        }
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
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = { navController.popBackStack() }
            ) {
                Text(stringResource(R.string.common_btn_back))
            }


            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (userId != null && user != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    contentPadding = PaddingValues(vertical = btnPaddingV),
                    onClick = {
                        scope.launch {
                            if (userId == null) return@launch
                            loading = true
                            try {
                                repo.softDelete(userId)
                                sessionStore.clear()

                                navController.navigate(Routes.AUTH) {
                                    popUpTo(Routes.PETS) { inclusive = true }
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_profile_delete_failed) + ": " + (e.message ?: ""))
                            } finally {
                                loading = false
                            }
                        }
                    }


                ) {
                    Text(stringResource(R.string.profile_btn_delete))
                }
            }


            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = {
                    sessionStore.clear()
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.PETS) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            ) {
                Text(stringResource(R.string.profile_btn_logout))
            }
        }
    }
}
