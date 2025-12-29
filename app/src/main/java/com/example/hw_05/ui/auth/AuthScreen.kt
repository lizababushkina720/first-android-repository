package com.example.hw_05.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.example.hw_05.R
import com.example.hw_05.Validators
import com.example.hw_05.data.LoginResult
import com.example.hw_05.data.prefs.SessionStore
import com.example.hw_05.di.ServiceLocator
import com.example.hw_05.ui.nav.Routes
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@Composable
fun AuthScreen(
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
    val snackbarTopPadding = dimensionResource(R.dimen.v_space_8)


    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var loading by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val emailOk = Validators.isEmailValid(email)
    val passwordOk = Validators.isPasswordValid(password)
    val formOk = emailOk && passwordOk

    val emailError = if (showErrors && !emailOk) stringResource(R.string.error_invalid_email) else null
    val passwordError = if (showErrors && !passwordOk) stringResource(R.string.error_password_short) else null

    fun submit() {
        scope.launch {
            showErrors = true
            if (!formOk) {
                snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_fix_errors))
                return@launch
            }

            try {
                loading = true
                val result = repo.login(email = email.trim(), password = password)


                when (result) {
                    is LoginResult.Success -> {
                        sessionStore.setCurrentUserId(result.user.id)
                        navController.navigate(Routes.PETS) {
                            popUpTo(Routes.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    LoginResult.UserNotFound -> {
                        loading = false
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_user_not_found))
                    }

                    LoginResult.BadCredentials -> {
                        loading = false
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_bad_credentials))
                    }

                    LoginResult.EmptyFields -> {
                        loading = false
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_empty_email_password))
                    }

                    is LoginResult.NeedRecovery -> {
                        navController.navigate(Routes.recoveryRoute(result.userId))
                    }

                    LoginResult.DeletedForever -> {
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_deleted_forever))
                    }
                }

            } catch (e: Exception) {
                snackbarHostState.showSnackbar(
                    ctx.getString(R.string.snackbar_login_failed) + ": " + (e.message ?: "")
                )
            } finally {
                loading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(screenPadding),
                verticalArrangement = Arrangement.spacedBy(vSpace12)
            ) {
                if (loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = stringResource(R.string.auth_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.field_email)) },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = { if (emailError != null) Text(emailError) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.field_password)) },
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = { if (passwordError != null) Text(passwordError) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = stringResource(R.string.cd_toggle_password_visibility)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    contentPadding = PaddingValues(vertical = btnPaddingV),
                    onClick = { submit() }
                ) {
                    Text(
                        text = if (loading) {
                            stringResource(R.string.auth_btn_sign_in_loading)
                        } else {
                            stringResource(R.string.auth_btn_sign_in)
                        }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    contentPadding = PaddingValues(vertical = btnPaddingV),
                    onClick = { navController.navigate(Routes.REGISTER) }
                ) {
                    Text(stringResource(R.string.auth_btn_register))
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
