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
import com.example.hw_05.data.RegisterResult
import com.example.hw_05.data.prefs.SessionStore
import com.example.hw_05.di.ServiceLocator
import com.example.hw_05.ui.nav.Routes
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator


@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val ctx = LocalContext.current
    val repo = remember { ServiceLocator.userRepository }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)
    val snackbarTopPadding = dimensionResource(R.dimen.v_space_8)


    var name by rememberSaveable { mutableStateOf("") }
    var nickname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }

    var loading by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameOk = name.trim().isNotBlank()
    val nicknameOk = nickname.trim().isNotBlank()
    val emailOk = Validators.isEmailValid(email)
    val passwordOk = Validators.isPasswordValid(password)
    val repeatOk = password == repeatPassword && passwordOk

    val formOk = nameOk && nicknameOk && emailOk && repeatOk

    val nameError = if (showErrors && !nameOk) stringResource(R.string.error_required) else null
    val nicknameError = if (showErrors && !nicknameOk) stringResource(R.string.error_required) else null
    val emailError = if (showErrors && !emailOk) stringResource(R.string.error_invalid_email) else null
    val passwordError = if (showErrors && !passwordOk) stringResource(R.string.error_password_short) else null
    val repeatError = if (showErrors && !repeatOk) stringResource(R.string.error_passwords_mismatch) else null

    val focusName = remember { FocusRequester() }
    val focusNick = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val focusPass = remember { FocusRequester() }
    val focusRepeat = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusName.requestFocus()
    }

    fun submit() {
        scope.launch {
            showErrors = true
            if (!formOk) {
                snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_fix_errors))
                return@launch
            }

            try {
                loading = true
                val result = repo.register(
                    name = name.trim(),
                    nickname = nickname.trim(),
                    email = email.trim(),
                    password = password
                )

                when (result) {
                    is RegisterResult.Success -> {
                        navController.navigate(Routes.REGISTER_OK) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    RegisterResult.UserExists -> {
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_user_exists))
                    }

                    RegisterResult.EmptyFields -> {
                        snackbarHostState.showSnackbar(ctx.getString(R.string.snackbar_empty_fields))
                    }
                }

            } catch (e: Exception) {
                snackbarHostState.showSnackbar(
                    ctx.getString(R.string.snackbar_register_failed) + ": " + (e.message ?: "")
                )
            } finally {
                loading = false
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
                    text = stringResource(R.string.register_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusName),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.field_name)) },
                    singleLine = true,
                    isError = nameError != null,
                    supportingText = { if (nameError != null) Text(nameError) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusNick.requestFocus() })
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusNick),
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text(stringResource(R.string.field_nickname)) },
                    singleLine = true,
                    isError = nicknameError != null,
                    supportingText = { if (nicknameError != null) Text(nicknameError) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusEmail.requestFocus() })
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusEmail),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.field_email)) },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = { if (emailError != null) Text(emailError) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusPass.requestFocus() })
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusPass),
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusRepeat.requestFocus() })
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRepeat),
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    label = { Text(stringResource(R.string.field_repeat_password)) },
                    singleLine = true,
                    isError = repeatError != null,
                    supportingText = { if (repeatError != null) Text(repeatError) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            submit()
                        }
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
                            stringResource(R.string.register_btn_create_loading)
                        } else {
                            stringResource(R.string.register_btn_create)
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
