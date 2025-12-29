package com.example.hw_05.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
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
fun RecoveryScreen(
    navController: NavHostController,
    userId: Long
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

    fun goToAuth() {
        navController.navigate(Routes.AUTH) {
            popUpTo(Routes.AUTH)
            launchSingleTop = true
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
                text = stringResource(R.string.recovery_deleted_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = stringResource(R.string.recovery_deleted_body),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = {
                    scope.launch {
                        if (userId <= 0L) {
                            snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_recovery_bad_id))
                            return@launch
                        }

                        try {
                            loading = true
                            repo.restore(userId)
                            sessionStore.setCurrentUserId(userId)

                            snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_restore_ok))
                            navController.navigate(Routes.PETS) {
                                popUpTo(Routes.AUTH) { inclusive = true }
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar(
                                ctx.getString(R.string.snackbar_recovery_failed) + ": " + (e.message ?: "")
                            )
                        } finally {
                            loading = false
                        }
                    }
                }
            ) {
                Text(
                    text = if (loading) stringResource(R.string.recovery_btn_processing)
                    else stringResource(R.string.recovery_btn_restore)
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = {
                    scope.launch {
                        if (userId <= 0L) {
                            snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_recovery_bad_id))
                            return@launch
                        }

                        try {
                            loading = true
                            repo.hardDelete(userId)
                            sessionStore.clear()

                            snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_deleted_forever))
                            goToAuth()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar(
                                ctx.getString(R.string.snackbar_recovery_failed) + ": " + (e.message ?: "")
                            )
                        } finally {
                            loading = false
                        }
                    }
                }
            ) {
                Text(
                    text = if (loading) stringResource(R.string.recovery_btn_processing)
                    else stringResource(R.string.recovery_btn_delete_forever)
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
}
